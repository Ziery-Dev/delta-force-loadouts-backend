package com.ziery.DeltaForceLoadouts.service;


import com.ziery.DeltaForceLoadouts.dto.response.BuildDtoResponse;
import com.ziery.DeltaForceLoadouts.entity.Build;
import com.ziery.DeltaForceLoadouts.entity.BuildRating;
import com.ziery.DeltaForceLoadouts.exception.DadoNaoEncontradoException;
import com.ziery.DeltaForceLoadouts.repository.BuildRatingRepository;
import com.ziery.DeltaForceLoadouts.repository.BuildRepository;
import com.ziery.DeltaForceLoadouts.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BuildRatingService {
    private final BuildRatingRepository buildRatingRepository;
    private final BuildRepository buildRepository;


    public BuildDtoResponse rateBuild(User user, Long buildId, int value) {

        //busca a build no respositorio
        Build build = buildRepository.findById(buildId).orElseThrow(() -> new DadoNaoEncontradoException("Build não encontrada!"));

        //verifica se a build ja foi avaliada pelo usuario atual
        var existBuildRating = buildRatingRepository.findByUserAndBuild(user, build);

        if(existBuildRating.isEmpty()) { //se não foi avaliada, avalia inserindo o valor da avaliação (1 ou -1, like e dislike respectivamente)
            if (value == 1){
                build.setLikeCount(build.getLikeCount() + 1);
            } else if (value == -1) {
                build.setDislikeCount(build.getDislikeCount() + 1);
            }
            buildRatingRepository.save(new BuildRating( user, build, value));

        }

        else {

            BuildRating rating = existBuildRating.get();
            if (rating.getRating() == value) {  //se avalição for a mesma anterior, somente remove

                if (value == 1){
                    build.setLikeCount(build.getLikeCount() - 1);
                } else if (value == -1) {
                    build.setDislikeCount(build.getDislikeCount() - 1);
                }
                buildRatingRepository.delete(rating);


            } else {

                if (rating.getRating() == 1) {
                    build.setLikeCount(build.getLikeCount() - 1);
                    build.setDislikeCount(build.getDislikeCount() + 1);
                } else {
                    build.setDislikeCount(build.getDislikeCount() - 1);
                    build.setLikeCount(build.getLikeCount() + 1);
                }
                rating.setRating(value); //se a avaliação for diferente (substitue a avaliação anterior
                buildRatingRepository.save(rating);
            }
        }

        return new BuildDtoResponse(build, user);



    }



}
