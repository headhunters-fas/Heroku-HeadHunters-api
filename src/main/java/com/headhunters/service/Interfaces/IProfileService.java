package com.headhunters.service.Interfaces;

import com.headhunters.model.Profile;
import org.springframework.stereotype.Service;

@Service
public interface IProfileService extends IService<Profile> {

    Profile saveProfileWithOwner(Profile profile, String owner);
}
