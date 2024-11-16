package sopt.makers.authentication.support.exception.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record IdTokenResponse(@JsonProperty("id_token") String idToken) {}
