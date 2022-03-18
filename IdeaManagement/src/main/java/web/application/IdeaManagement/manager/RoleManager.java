package web.application.IdeaManagement.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.dto.PageDto;
import web.application.IdeaManagement.entity.Permission;
import web.application.IdeaManagement.entity.Role;
import web.application.IdeaManagement.entity.RolePermission;
import web.application.IdeaManagement.model.request.RoleRequest;
import web.application.IdeaManagement.model.response.PermissionResponse;
import web.application.IdeaManagement.model.response.RoleResponse;
import web.application.IdeaManagement.repository.RolePermissionRepository;
import web.application.IdeaManagement.repository.RoleRepository;
import web.application.IdeaManagement.specification.RoleSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleManager {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RolePermissionRepository rolePermissionRepo;
    @Autowired
    RoleSpecification roleSpecification;

    public Integer create(RoleRequest request, String email) {
        try {
            final String finalEmail = email;
            Boolean checkExisted = roleRepository.existsByName(request.getName());
            if (checkExisted) {
                return -2;
            }
            Role newRole = new Role();
            newRole.setName(request.getName());
            newRole.setCreatedUser(email);
            newRole.setCreatedDate(new Date());
            Role returnRole = roleRepository.save(newRole);
            if (request.getPermission() != null) {
                List<RolePermission> listRolePer = request.getPermission().stream().map(x -> {
                    RolePermission rolePer = new RolePermission(returnRole.getId(), x);
                    rolePer.setCreatedUser(finalEmail);
                    rolePer.setCreatedDate(new Date());
                    return rolePer;
                }).collect(Collectors.toList());
                rolePermissionRepo.saveAll(listRolePer);
            }
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public PageDto getRole(String search, Integer page, Integer limit, String sortBy, String sortType) {
        try {
            Integer pageNum = page - 1;
            Sort sort = responseUtils.getSort(sortBy, sortType);
            Page<Role> pageRes = roleRepository.findAll(roleSpecification.filter(search), PageRequest.of(pageNum, limit, sort));
            return PageDto.builder()
                    .content(pageRes.getContent())
                    .numberOfElements(page)
                    .page(page)
                    .size(limit)
                    .totalPages(pageRes.getTotalPages())
                    .totalElements(pageRes.getTotalElements())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }

}
