package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Department;
import web.application.IdeaManagement.entity.Role;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.model.request.SignupRequest;
import web.application.IdeaManagement.model.response.UserResponse;
import web.application.IdeaManagement.repository.DepartmentRepository;
import web.application.IdeaManagement.repository.RoleRepository;
import web.application.IdeaManagement.repository.UserRepository;
import web.application.IdeaManagement.specification.UserSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManager {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserSpecification userSpecification;

    public PageDto getUser(String search, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Integer pageNum = page - 1;
            Sort sort = responseUtils.getSort(sortBy, sortType);
            List<UserResponse> listFinal = new ArrayList<>();
            Page<User> listUser = userRepository.findAll(userSpecification.filter(search), PageRequest.of(pageNum, limit, sort));
            if (!listUser.getContent().isEmpty()) {
                List<Long> listUID = listUser.getContent().stream().map(user -> user.getDepartmentId()).collect(Collectors.toList());
                List<Department> listDept = departmentRepository.findAllById(listUID);
                Map<Long, Department> mapDeptName = listDept.stream().collect(Collectors.toMap(Department::getId, Function.identity()));

                listFinal = listUser.getContent().stream().map(x -> {
                    UserResponse newRes = new UserResponse();
                    newRes.setAddress(x.getAddress());
                    newRes.setSex(x.getSex());
                    newRes.setPhone(x.getPhone());
                    newRes.setEmail(x.getEmail());
                    newRes.setFirstname(x.getFirstname());
                    newRes.setLastname(x.getLastname());
                    newRes.setUserId(x.getUserId());
                    String roles = x.getRoles().isEmpty() ? null : x.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()).get(0);
                    newRes.setRole(roles);
                    Long roleId = x.getRoles().isEmpty() ? null : x.getRoles().stream().map(role -> role.getId()).collect(Collectors.toList()).get(0);
                    newRes.setRoleId(roleId);
                    newRes.setDepartment(mapDeptName.get(x.getDepartmentId()).getDepartment());
                    newRes.setDepartmentId(mapDeptName.get(x.getDepartmentId()).getId());
                    return newRes;
                }).collect(Collectors.toList());
            }
            return PageDto.builder()
                    .content(listFinal)
                    .numberOfElements(pageNum)
                    .page(page)
                    .size(limit)
                    .totalPages(listUser.getTotalPages())
                    .totalElements(listUser.getTotalElements())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return new PageDto();
        }
    }

    public Integer updateUser(String userId, SignupRequest req) {
        try {
            User editedUser = userRepository.findById(userId).get();
            if (req.getDepartmentId() != null && !Objects.equals(req.getDepartmentId(), editedUser.getDepartmentId())) {
                editedUser.setDepartmentId(req.getDepartmentId());
            }
            if (req.getFirstname() != null && !req.getFirstname().equals(editedUser.getFirstname())) {
                editedUser.setFirstname(req.getFirstname());
            }
            if (req.getLastname() != null && !req.getLastname().equals(editedUser.getLastname())) {
                editedUser.setLastname(req.getLastname());
            }
            if (req.getAddress() != null && !req.getAddress().equals(editedUser.getAddress())) {
                editedUser.setAddress(req.getAddress());
            }
            if (req.getPhone() != null && !req.getPhone().equals(editedUser.getPhone())) {
                editedUser.setPhone(req.getPhone());
            }
            if (req.getSex() != null && !req.getSex().equals(editedUser.getSex())) {
                editedUser.setSex(req.getSex());
            }

            // Update role
            Role roleDel = editedUser.getRoles().stream().findFirst().get();
            editedUser.getRoles().remove(roleDel);
            Role roleUpdate = roleRepository.findRoleById(req.getRoleId());
            editedUser.getRoles().add(roleUpdate);
            userRepository.save(editedUser);

            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
