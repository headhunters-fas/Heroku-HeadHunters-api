package com.headhunters.controller;

import com.headhunters.model.Profile;
import com.headhunters.service.impl.MapValidationErrorService;
import com.headhunters.service.impl.ProfileService;
import com.headhunters.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping("/users/profile")
    public ResponseEntity<?> addProfile(@Valid @RequestBody Profile profile, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap!=null) return errorMap;

        Profile newProfile = profileService.saveProfileWithOwner(profile, principal.getName());

        return new ResponseEntity<Profile>(newProfile, HttpStatus.CREATED);
    }

    @PutMapping("/users/{profileId}")
    public ResponseEntity<String> updateProfile(@RequestBody Profile profileRequest, @PathVariable Long profileId){

        Profile oldProfile = profileService.findById(profileId);
        oldProfile.setAccountType(profileRequest.getAccountType());
        oldProfile.setBandDescription(profileRequest.getBandDescription());
        oldProfile.setBandImageUrl(profileRequest.getBandImageUrl());
        oldProfile.setBandMembers(profileRequest.getBandMembers());
        oldProfile.setBandName(profileRequest.getBandName());
        oldProfile.setLinkToSample(profileRequest.getLinkToSample());

        profileService.save(oldProfile);

        return new ResponseEntity<String>("Profile updated", HttpStatus.OK);
    }

    @GetMapping("/users/profile")
    public Profile findProfileByUser(Principal principal){
        return profileService.findByUser(principal.getName());
    }
}
