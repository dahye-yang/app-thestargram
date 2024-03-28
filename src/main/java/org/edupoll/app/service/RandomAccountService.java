package org.edupoll.app.service;

import java.util.List;

import org.edupoll.app.entity.Profile;
import org.edupoll.app.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RandomAccountService {
	
	private final ProfileRepository profileRepository;
	
	public List<Profile> randomAccountAndProfile(Long accountId){
		
		return profileRepository.findAll(accountId);
	}
	
}
