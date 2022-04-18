//package web.application.IdeaManagement.config;
//
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.commons.lang3.time.DateUtils;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import web.application.IdeaManagement.entity.Permission;
//import web.application.IdeaManagement.entity.Role;
//import web.application.IdeaManagement.entity.RolePermission;
//import web.application.IdeaManagement.entity.User;
//import web.application.IdeaManagement.repository.PermissionRepository;
//import web.application.IdeaManagement.repository.RolePermissionRepository;
//import web.application.IdeaManagement.repository.RoleRepository;
//import web.application.IdeaManagement.repository.UserRepository;
//
//import java.util.*;
//
//@Component
//public class SetupDefaultData implements InitializingBean {
//    @Autowired
//    RoleRepository roleRepository;
//    @Autowired
//    PermissionRepository permissionRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    RolePermissionRepository rolePermissionRepo;
//    @Autowired
//    PasswordEncoder encoder;
//
//    @Override
// 	public void afterPropertiesSet() throws Exception {
//        //---------------------Add permission-------------------------//
//        List<String> permName = Arrays.asList("Manage User", "Manage Role", "Manager Semester", "Manage Department", "Manage Topic", "Manage Category");
//        List<String> permFlag = Arrays.asList("MUSER", "MROLE", "MSEMES", "MDEPT", "MTOPIC", "MCATE");
//        List<Permission> listPermExist = permissionRepository.findByFlagIn(permFlag);
//        List<Permission> listPerm = new ArrayList<>();
//        if ((!listPermExist.isEmpty() && listPermExist.size() != 6) || listPermExist.isEmpty()) {
//            permissionRepository.deleteByFlagIn(permFlag);
//            for (int i = 0; i < 6; i++) {
//                Permission newPerm = new Permission();
//                newPerm.setCreatedUser("SYSTEM");
//                newPerm.setCreatedDate(new Date());
//                newPerm.setFlag(permFlag.get(i));
//                newPerm.setName(permName.get(i));
//                listPerm.add(newPerm);
//            }
//        }
//        listPerm = permissionRepository.saveAll(listPerm);
//        //---------------------Add default role-----------------------//
//        List<Role> defaultRole = roleRepository.getRoleByNameIn(Arrays.asList("ADMIN, STAFF, QA_COORDINATOR"));
//        List<Role> roles = new ArrayList<>();
//        if ((!defaultRole.isEmpty() && defaultRole.size() != 3) || defaultRole.isEmpty()) {
//            roleRepository.deleteByNameIn(Arrays.asList("ADMIN, STAFF, QA_COORDINATOR"));
//            Role roleAdmin = new Role();
//            Role roleStaff = new Role();
//            Role roleQA = new Role();
//
//            roleAdmin.setCreatedDate(new Date());
//            roleAdmin.setCreatedUser("SYSTEM");
//            roleAdmin.setName("ADMIN");
//            roleAdmin.setSystem(1);
//
//            roleStaff.setCreatedDate(new Date());
//            roleStaff.setCreatedUser("SYSTEM");
//            roleStaff.setName("STAFF");
//            roleStaff.setSystem(1);
//
//            roleQA.setCreatedDate(new Date());
//            roleQA.setCreatedUser("SYSTEM");
//            roleQA.setName("QA_COORDINATOR");
//            roleQA.setSystem(1);
//
//            roles.add(roleAdmin);
//            roles.add(roleStaff);
//            roles.add(roleQA);
//
//        }
//        defaultRole = roleRepository.saveAll(roles);
//        List<RolePermission> listRolePerm = new ArrayList<>();
//        for (Role rol : defaultRole) {
//            if (rol.getName().equals("ADMIN")) {
//                for (int i = 0; i < 6; i++) {
//                    RolePermission newRolePerm = new RolePermission();
//                    newRolePerm.setCreatedDate(new Date());
//                    newRolePerm.setCreatedUser("SYSTEM");
//                    newRolePerm.setRoleId(rol.getId());
//                    newRolePerm.setPermissionId(Long.valueOf(i));
//                    listRolePerm.add(newRolePerm);
//                }
//            } else {
//
//            }
//        }
//        rolePermissionRepo.saveAll(listRolePerm);
//        User defaultUser = userRepository.findByEmail("dinhkhiem@gmail.com");
//        if (defaultUser != null) {
//            String userId = UUID.randomUUID().toString();
//            String randomPass = "1234";
//            User user = new User();
//            Role adminRole = defaultRole.stream().filter(x -> x.getName().equals("ADMIN")).findFirst().orElse(null);
//            Set<Role> userRole = new HashSet<>();
//            userRole.add(adminRole);
//            user.setDepartmentId(1l);
//            user.setFirstname("Khiem");
//            user.setLastname("Pham");
//            user.setPhone("SYSTEM");
//            user.setAddress("SYSTEM");
//            user.setUsername("dinhkhiem@gmail.com");
//            user.setPassword(encoder.encode(randomPass));
//            user.setSex("male");
//            user.setRoles(userRole);
//            user.setUserId(userId);
//            user.setCreateDate(new Date());
//            user.setCreatedUser("khiem");
//            user.setOtpExpired(DateUtils.addMinutes(new Date(), 10));
//            userRepository.save(user);
//        }
//    }
//}
