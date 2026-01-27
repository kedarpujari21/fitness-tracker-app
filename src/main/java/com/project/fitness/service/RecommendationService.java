package com.project.fitness.service;

import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.model.Activity;
import com.project.fitness.model.Recommendation;
import com.project.fitness.model.User;
import com.project.fitness.repository.ActivityRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RecommendationRepository recommendationRepository;
    public Recommendation generateRecommendation(RecommendationRequest recommendationRequest) {
               User user=userRepository.findById(recommendationRequest.getUserId())
                       .orElseThrow(()-> new RuntimeException("User WIth Given Id Not Exists : " + recommendationRequest.getUserId()));

               Activity activity = activityRepository.findById(recommendationRequest.getActivityId())
                   .orElseThrow(()-> new RuntimeException("User WIth Given Id Not Exists : " + recommendationRequest.getActivityId()));

               Recommendation recommendation=Recommendation.builder()
                       .user(user)
                       .activity(activity)
                       .recommendation(recommendationRequest.getRecommendation())
                       .improvements(recommendationRequest.getImprovements())
                       .suggestions(recommendationRequest.getSuggestions())
                       .safety(recommendationRequest.getSafety())
                       .build();

               Recommendation savedRecommendation=recommendationRepository.save(recommendation);
               return savedRecommendation;
    }

    public List<Recommendation> getUserRecommendations(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendations(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
