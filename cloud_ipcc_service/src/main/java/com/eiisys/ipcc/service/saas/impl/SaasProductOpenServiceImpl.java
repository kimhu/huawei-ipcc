package com.eiisys.ipcc.saas.service.impl;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eiisys.ipcc.bean.saas.SaasOpenProductBean;
import com.eiisys.ipcc.constants.IpccConstants;
import com.eiisys.ipcc.core.utils.TimeUtils;
import com.eiisys.ipcc.dao.AddressDao;
import com.eiisys.ipcc.dao.CompanyConfigDao;
import com.eiisys.ipcc.dao.PermissionDao;
import com.eiisys.ipcc.dao.RoleAndPermissionDao;
import com.eiisys.ipcc.dao.RoleDao;
import com.eiisys.ipcc.dao.UserAndRoleDao;
import com.eiisys.ipcc.entity.Company;
import com.eiisys.ipcc.entity.CompanyConfig;
import com.eiisys.ipcc.entity.Permission;
import com.eiisys.ipcc.entity.Role;
import com.eiisys.ipcc.entity.RoleAndPermission;
import com.eiisys.ipcc.entity.User;
import com.eiisys.ipcc.entity.UserAndRole;
import com.eiisys.ipcc.saas.service.SaasProductOpenService;
import com.eiisys.ipcc.service.CompanyService;
import com.eiisys.ipcc.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SaasProductOpenServiceImpl implements SaasProductOpenService {
    @Value("${saas.53kf.secret}")
    private String saasSecret;

    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permDao;
    @Autowired
    private UserAndRoleDao urDao;
    @Autowired
    private RoleAndPermissionDao rpDao;
    @Autowired
    private CompanyConfigDao configDao;
    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @Override
    public String generateToken(SaasOpenProductBean bean) {
        String md5Code = null;
        try {
            Map<String, String> requestMap = new HashMap<String, String>();
            List<String> request = new ArrayList<String>();
            Field[] fields = bean.getClass().getDeclaredFields();
            Field.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                String value = (String) fields[i].get(bean);
                if (StringUtils.isNotBlank(value)) {
                    // value = URLDecoder.decode(value, "UTF-8");
                    fields[i].set(bean, value);
                    if (!fields[i].getName().equals("token")) {
                        request.add(name);
                        requestMap.put(name, value);
                    }
                }
            }
            String[] arr = request.toArray(new String[0]);
            Arrays.sort(arr);
            List<String> result = new ArrayList<String>();
            for (String key : arr) {
                String nameAndValue = key + "=" + requestMap.get(key);
                result.add(nameAndValue);
            }

            String requestStr = StringUtils.join(result, "&") + saasSecret;
            log.info("[Product Opening......] generate token param: " + requestStr);
            byte[] secretBytes = MessageDigest.getInstance("md5").digest(requestStr.getBytes());
            md5Code = new BigInteger(1, secretBytes).toString(16);
            for (int i = 0; i < 32 - md5Code.length(); i++) {
                md5Code = "0" + md5Code;
            }
            log.info("[Product Opening......] token MD5: " + md5Code);
        } catch (Exception e) {
            log.error("[Product Opening......] generate token exception: {}", e);
        }
        return md5Code;
    }

    /**
     * 初始化企业和主账号
     * 
     * @param company 公司信息
     * @param user 用户信息
     * @param isHuawe 是否是华为云市场
     * @throws Exception
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int initCompany(Company company, User user, boolean isHuawei) throws Exception {
        /** 初始化用户begin **/
        Integer id6d = user.getId6d();
        int result = userService.saveUserSelective(user);
        log.info("[Product Open Service] save user {} result: {}", id6d, result);
        if (result == 0) {
            log.error("[Product Open Service] save user info failed, id6d: {}", id6d);
            throw new Exception();
        }
        /** 初始化用户end **/

        /** 如果公司不存在，初始化公司，否则更新过期时间 **/
        Integer companyId = company.getCompanyId();
        Company dbCompany = companyService.getCompanyByCompanyId(companyId);
        if (dbCompany == null) {
            /** 初始化公司begin **/
            /** 创建公司 **/
            result = companyService.saveCompanySelective(company);
            log.info("[Product Open Service] save company, result: " + result);
            if (result == 0) {
                log.error("[Product Open Service] save company info failed, company id: {}", companyId);
                throw new Exception();
            }

            Date date = new Date();
            /** 创建角色 **/
            // 创建超级管理员
            Role rootRole = new Role();
            rootRole.setCompanyId(companyId);
            rootRole.setRoleName(IpccConstants.ATTENDANT_ROOT_NAME);
            rootRole.setDescription(IpccConstants.ATTENDANT_ROOT_DESC);
            rootRole.setRoleType(IpccConstants.ATTENDANT_TYPE_FIXED);
            rootRole.setIsDeleted((byte) 0);
            rootRole.setCreatedAt(date);
            rootRole.setUpdatedAt(date);
            // 创建班长坐席
            Role leadRole = new Role();
            leadRole.setCompanyId(companyId);
            leadRole.setRoleName(IpccConstants.ATTENDANT_LEAD_NAME);
            leadRole.setDescription(IpccConstants.ATTENDANT_LEAD_DESC);
            leadRole.setRoleType(IpccConstants.ATTENDANT_TYPE_FIXED);
            leadRole.setIsDeleted((byte) 0);
            leadRole.setCreatedAt(date);
            leadRole.setUpdatedAt(date);
            // 创建普通坐席
            Role normalRole = new Role();
            normalRole.setCompanyId(companyId);
            normalRole.setRoleName(IpccConstants.ATTENDANT_NORMAL_NAME);
            normalRole.setDescription(IpccConstants.ATTENDANT_NORMAL_DESC);
            normalRole.setRoleType(IpccConstants.ATTENDANT_TYPE_FIXED);
            normalRole.setIsDeleted((byte) 0);
            normalRole.setCreatedAt(date);
            normalRole.setUpdatedAt(date);
            // 创建销售主管
            Role saleRole = new Role();
            saleRole.setCompanyId(companyId);
            saleRole.setRoleName(IpccConstants.ATTENDANT_SALES_NAME);
            saleRole.setDescription(IpccConstants.ATTENDANT_SALES_DESC);
            saleRole.setRoleType(IpccConstants.ATTENDANT_TYPE_FIXED);
            saleRole.setIsDeleted((byte) 0);
            saleRole.setCreatedAt(date);
            saleRole.setUpdatedAt(date);

            int roleId = roleDao.insertUseGeneratedKeys(rootRole);
            log.info("[Product Open Service] saving admin role, result: " + roleId);
            if (roleId == 0) {
                log.error("[Product Open Service] save admin role info error company id: {}", companyId);
                throw new Exception();
            }
            int leadId = roleDao.insertUseGeneratedKeys(leadRole);
            log.debug("[Product Open Service] saving leader role, result: " + leadId);
            if (leadId == 0) {
                log.error("[Product Open Service] save lead role info error company id: {}", companyId);
                throw new Exception();
            }
            int normalId = roleDao.insertUseGeneratedKeys(normalRole);
            log.debug("[Product Open Service] saving normal role, result: " + normalId);
            if (normalId == 0) {
                log.error("[Product Open Service] save normal role info error company id: {}", companyId);
                throw new Exception();
            }
            int saleId = roleDao.insertUseGeneratedKeys(saleRole);
            log.debug("[Product Open Service] saving sales role, result: " + saleId);
            if (saleId == 0) {
                log.error("[Product Open Service] save sales role info error company id: {}", companyId);
                throw new Exception();
            }

            /** 关联角色 **/
            UserAndRole userRole = new UserAndRole();
            userRole.setCompanyId(companyId);
            userRole.setUserId6d(user.getId6d());
            userRole.setRoleId(rootRole.getId());
            result = urDao.saveUserAndRoleSelective(userRole);
            log.debug("[Saas Business Service] saving admin related role, result: " + result);
            if (result == 0) {
                log.error("[Saas Business Service] save user and role info error, company id: {}", companyId);
                throw new Exception();
            }

            /** 关联角色与权限 **/
            List<Permission> permissions = permDao.getPermissionList();
            if (!permissions.isEmpty()) {
                /** 关联列表 **/
                List<RoleAndPermission> permissionList = new ArrayList<>();

                for (Permission permission : permissions) {
                    String code = permission.getPermissionCode();
                    /** 普通坐席 **/
                    if (code.equals(IpccConstants.CALL_HISTORY_PERSON)) {
                        RoleAndPermission normalPermission = new RoleAndPermission();
                        normalPermission.setCompanyId(companyId);
                        normalPermission.setPermissionId(permission.getId());
                        normalPermission.setRoleId(normalRole.getId());
                        normalPermission.setIsDeleted((byte) 0);
                        permissionList.add(normalPermission);
                        continue;
                    }

                    /** 班长坐席和销售主管可以对成语管理进行增删改看 **/
                    if (code.equals(IpccConstants.CUSTOMER_MANAGE_ORDER)
                            || code.equals(IpccConstants.CUSTOMER_MANAGE_ADD)
                            || code.equals(IpccConstants.CUSTOMER_MANAGE_EDIT)
                            || code.equals(IpccConstants.CUSTOMER_MANAGE_DELETE)) {
                        /** 班长坐席 **/
                        RoleAndPermission leadPermission = new RoleAndPermission();
                        leadPermission.setCompanyId(companyId);
                        leadPermission.setPermissionId(permission.getId());
                        leadPermission.setRoleId(leadRole.getId());
                        leadPermission.setIsDeleted((byte) 0);
                        permissionList.add(leadPermission);
                        /** 销售主管 **/
                        RoleAndPermission salePermission = new RoleAndPermission();
                        salePermission.setCompanyId(companyId);
                        salePermission.setPermissionId(permission.getId());
                        salePermission.setRoleId(saleRole.getId());
                        salePermission.setIsDeleted((byte) 0);
                        permissionList.add(salePermission);
                    }

                    /** 班长坐席可以查看本组组的历史通话记录 **/
                    if (code.equals(IpccConstants.CALL_HISTORY_GROUP)) {
                        RoleAndPermission leadPermission = new RoleAndPermission();
                        leadPermission.setCompanyId(companyId);
                        leadPermission.setPermissionId(permission.getId());
                        leadPermission.setRoleId(leadRole.getId());
                        leadPermission.setIsDeleted((byte) 0);
                        permissionList.add(leadPermission);
                        continue;
                    }

                    /** 销售主管可以分配和导出客户信息 **/
                    if (code.equals(IpccConstants.CUSTOMER_MANAGE_EXPORT)
                            || code.equals(IpccConstants.CUSTOMER_MANAGE_ASSIGN)) {
                        RoleAndPermission salePermission = new RoleAndPermission();
                        salePermission.setCompanyId(companyId);
                        salePermission.setPermissionId(permission.getId());
                        salePermission.setRoleId(saleRole.getId());
                        salePermission.setIsDeleted((byte) 0);
                        permissionList.add(salePermission);
                    }

                    /** 管理员拥有所有权限 **/
                    RoleAndPermission rootPermission = new RoleAndPermission();
                    rootPermission.setCompanyId(companyId);
                    rootPermission.setPermissionId(permission.getId());
                    rootPermission.setRoleId(rootRole.getId());
                    rootPermission.setIsDeleted((byte) 0);
                    permissionList.add(rootPermission);
                }

                result = rpDao.insertList(permissionList);
                log.debug("[Saas Business Service] saving permission list, result: " + result);
                if (result != permissionList.size()) {
                    log.error("[Saas Business Service] save role and permission info error, company id: {}", companyId);
                    throw new Exception();
                }
            }

            /** 保存配置文件，是否是华为账号 **/
            CompanyConfig config = new CompanyConfig();
            config.setCompanyId(companyId);
            config.setConfigId(IpccConstants.IPCC_IS_HUAWEI);
            if (isHuawei) {
                config.setConfigValue(String.valueOf(1));
            } else {
                config.setConfigValue(String.valueOf(0));
            }
            result = configDao.insertSelective(config);
            log.debug("[Product Open Service] save company config, result: " + result);
            if (result == 0) {
                log.error("[Product Open Service] save config info error, company id: {}", companyId);
                throw new Exception();
            }

            /** 创建客户资源信息表 **/
            addressDao.createAddressTable(companyId);
            /** 初始化公司end **/
        } else {
            dbCompany.setExpirationTime(company.getExpirationTime());
            result = companyService.updateCompany(dbCompany);
            log.info("[Product Open Service] update company {} result: {}", companyId, result);
            if (result == 0) {
                log.info("[Product Open Service] update company info failed, company id: {}", companyId);
                throw new Exception();
            }
        }

        return result;
    }

    /**
     * 初始化用户信息
     * 
     * @param user 用户信息
     * @throws Exception
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int initNormalUser(User user) throws Exception {
        /** 关联普通坐席 **/
        Integer companyId = user.getCompanyId();
        int roleId = roleDao.getRoleIdByName(companyId, IpccConstants.ATTENDANT_NORMAL_NAME);
        UserAndRole userAndRole = new UserAndRole();
        userAndRole.setCompanyId(companyId);
        userAndRole.setUserId6d(user.getId6d());
        userAndRole.setRoleId(roleId);
        int result = urDao.saveUserAndRoleSelective(userAndRole);
        log.info("[Product Open Service] saving normal related role result: {}", result);
        if (result == 0) {
            log.error("[Product Open Service] save normal related role failed, id6d: {}", user.getId6d());
            throw new Exception();
        }

        result = userService.saveUserSelective(user);
        log.info("[Product Open Service] save normal user {} result {}", user.getId6d(), result);
        if (result == 0) {
            log.error("[Product Open Service] save normal user failed, id6d: {}", user.getId6d());
            throw new Exception();
        }
        return result;
    }

    /**
     * 开通公司或者延长过期时间
     * 
     * @param bean
     * @throws Exception
     * @return -1普通坐席但公司不存在 -2推送不合法0失败 1成功
     */
    @Override
    public int openProduct(SaasOpenProductBean bean) throws Exception {
        int result = 0;
        boolean isAdmin = false;
        String isAdminReq = bean.getIs_superAdmin();
        if (isAdminReq != null && Integer.valueOf(isAdminReq) == 1) {
            isAdmin = true;
        }

        int id6d = Integer.valueOf(bean.getId6d());
        int companyId = Integer.valueOf(bean.getCompany_id());
        User user = userService.getUserById6d(id6d);
        Company company = companyService.getCompanyByCompanyId(companyId);

        if (!isAdmin && company == null) {
            log.error("[Product Open Service] normal user with not exist company");
            return -1;
        }

        String expireTime = bean.getExpiration_time();
        Date expireDate = TimeUtils.toDate(expireTime, TimeUtils.DATE_FORMAT);

        boolean isHuawei = true;
        /** 账号不存在，初始化公司和主账号 **/
        if (user == null) {
            /** 初始化用户 **/
            user = new User();
            user.setCompanyId(companyId);
            user.setId6d(id6d);
            user.setIsAdmin((byte) (isAdmin ? 1 : 0));
            String genderStr = bean.getGender();
            byte gender = StringUtils.isEmpty(genderStr) ? 1 : Byte.valueOf(genderStr);
            user.setGender(gender);
            user.setUserId(StringUtils.isEmpty(bean.getAccount()) ? null : bean.getAccount());
            user.setExpirationTime(expireDate);
            user.setPhone(StringUtils.isEmpty(bean.getPhone()) ? null : bean.getPhone());
            String userName = bean.getName();
            userName = StringUtils.isEmpty(userName) ? null : userName;
            if (userName != null) {
                userName = URLDecoder.decode(userName, "UTF-8");
            }
            user.setUserName(userName);

            /** 如果是主账号，初始化企业和主账号；如果是普通账号，只需初始化普通账号 **/
            if (isAdmin) {
                /** 初始化企业 **/
                company = new Company();
                company.setCompanyId(companyId);
                String companyName = bean.getCompany_name();
                companyName = StringUtils.isEmpty(companyName) ? null : companyName;
                if (companyName != null) {
                    companyName = URLDecoder.decode(companyName, "UTF-8");
                }
                company.setCompanyName(companyName);
                company.setContacts(userName);
                company.setPhone(StringUtils.isEmpty(bean.getPhone()) ? null : bean.getPhone());
                company.setExpirationTime(expireDate);

                result = initCompany(company, user, isHuawei);
            } else {
                result = initNormalUser(user);
            }
            return result;
        }

        /** 账号存在，延长过期时间 **/
        /** 华为账号直接更新过期时间，53账号需要判断推送是否合法 **/
        if (!isHuawei) {
            boolean rerodLegal = isRecordLegal(expireDate, id6d);
            if (!rerodLegal) {
                return -2;
            }
        }
        
        user.setExpirationTime(expireDate);
        result = userService.updateUser(user);
        log.info("[Product Open Service] update admin user {} result {}", id6d, result);
        if (result == 0) {
            log.error("[Product Open Service] update admin user info failed, id6d: {}", id6d);
            throw new Exception();
        }
        return result;
    }

    private boolean isRecordLegal(Date saasExireDate, Integer id6d) {
        boolean result = false;
        /** 用户到期时间 **/
        // Date expireDate = dbUser.getExpirationTime();
        /** 过期用户进行续购，如果是月中续购，则过期时间是当月月底，否则是下月月底 **/
        Date expireDate = TimeUtils.getLastDateOfTheMonth(new Date());
        /** 用户下月到期时间 **/
        Date nextExpireDate = TimeUtils.getNextMonthDay(expireDate);
        String expireTime = TimeUtils.dateFormat(expireDate, TimeUtils.DATE_FORMAT);
        String nextExpireTime = TimeUtils.dateFormat(nextExpireDate, TimeUtils.DATE_FORMAT);
        String saasExpireTime = TimeUtils.dateFormat(saasExireDate, TimeUtils.DATE_FORMAT);
        result = !saasExpireTime.equals(expireTime) && !saasExpireTime.equals(nextExpireTime);
        if (result) {
            log.debug("[Saas Business Service] expiration date does not match, db next expiration date: {},"
                    + " saas next expiration date: {}, id6d: {}", nextExpireDate, saasExireDate, id6d);
        }
        return result;
    }
}
