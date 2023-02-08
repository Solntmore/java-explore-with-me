package ru.practicum.ewmserv.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewmserv.request.dto.ResponseRequestDto;

import java.util.ArrayList;

@AllArgsConstructor
@Builder
@Jacksonized
@Data
public class ResultOfUpdateRequests {

    private final ArrayList<ResponseRequestDto> confirmedRequests;

    private final ArrayList<ResponseRequestDto> rejectedRequests;

}
