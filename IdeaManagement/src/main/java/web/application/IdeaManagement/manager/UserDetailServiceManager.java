package web.application.IdeaManagement.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.application.IdeaManagement.entity.User;
import web.application.IdeaManagement.repository.UserRepository;

@Service
public class UserDetailServiceManager implements UserDetailsService {
    @Autowired
    UserRepository userDao;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailManager.build(user);
    }
}
