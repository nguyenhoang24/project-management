package utc.edu.thesis.service;

import utc.edu.thesis.domain.entity.OldPassword;

public interface IOldPasswordService extends IService<OldPassword>{
    Iterable<OldPassword> findAllByUserIdTop3OldPassword(Long id);
}
