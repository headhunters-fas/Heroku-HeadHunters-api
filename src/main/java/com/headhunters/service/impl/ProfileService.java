package com.headhunters.service.impl;

import com.headhunters.model.Profile;
import com.headhunters.model.User;
import com.headhunters.repository.ProfileRepository;
import com.headhunters.repository.UserRepository;
import com.headhunters.service.Interfaces.IProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService implements IProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }

    @Override
    public Iterable<Profile> findAll() {
        return profileRepository.findAll();
    }

    @Override
    public Profile findById(Long id) {
        return profileRepository.getById(id);
    }

    @Override
    public void delete(Long id, String owner) {
        Profile profile = findById(id);
        profileRepository.delete(profile);
    }

    @Override
    public Profile saveProfileWithOwner(Profile profile, String owner) {
        try {
            User user = userRepository.findByUsername(owner);
            profile.setUsername(user.getUsername());
            profile.setUser(user);
            profileRepository.save(profile);
        } catch (Exception e){
            throw new RuntimeException("album ID '"+profile.getAccountType()+"' already exists");
        }

        return null;
    }

    public Profile findByUser(String username){
        return profileRepository.findByUsername(username);
    }
}
