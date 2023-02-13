package ru.practicum.ewmserv.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.ewmserv.enums.RequestUpdateState;

import java.util.ArrayList;

@AllArgsConstructor
@Builder
@Jacksonized
@Data
public class UpdateListForRequests {

    private final ArrayList<Long> requestIds;

    private final RequestUpdateState status;

}
