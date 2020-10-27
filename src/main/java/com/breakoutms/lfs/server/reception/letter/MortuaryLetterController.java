package com.breakoutms.lfs.server.reception.letter;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.RECEPTION+"/mortuary-letter")
@AllArgsConstructor
public class MortuaryLetterController {

	private final MortuaryLetterService service;
	
	@GetMapping("/{tagNo}")
	public Map<String, Object> getLetter(@PathVariable String tagNo) {
		return service.create(tagNo);
	}
	


}
