package com.breakoutms.lfs.server.preneed.model;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class DependentViewModel extends RepresentationModel<DependentViewModel>{
	private String id;
    private String names;
    private String surname;
    private LocalDate dateOfBirth;
	private String relationship;
    private String phoneNumber;
	private boolean deceased;
}
