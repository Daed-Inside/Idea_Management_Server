package web.application.IdeaManagement.manager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.Permission;
import web.application.IdeaManagement.model.response.PermissionResponse;
import web.application.IdeaManagement.repository.PermissionRepository;
import web.application.IdeaManagement.repository.RolePermissionRepository;
import web.application.IdeaManagement.repository.RoleRepository;
import web.application.IdeaManagement.specification.RoleSpecification;
import web.application.IdeaManagement.utils.ResponseUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionManager {
    @Autowired
    ResponseUtils responseUtils;
    @Autowired
    PermissionRepository permissionRepository;
    @Autowired
    ModelMapper modelMapper;

    public List<PermissionResponse> getPermission() {
        List<Permission> listPermission = permissionRepository.findAll();
        return listPermission.stream().map(x -> modelMapper.map(x, PermissionResponse.class)).collect(Collectors.toList());
    }
}
