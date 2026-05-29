package com.studentmanagement.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ScoreBatchRequest {
    @NotNull
    public Long subjectId;
    
    @NotNull
    public Long semesterId;

    @Valid
    @NotNull
    public List<ScoreRequest> scores;
}
