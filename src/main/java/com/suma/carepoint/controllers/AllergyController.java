package com.suma.carepoint.controllers;

import com.suma.carepoint.models.constants.ApiConstant;
import com.suma.carepoint.services.emr.AllergyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiConstant.Controller.PROCEDURE)
@AllArgsConstructor
public class AllergyController {
    @Autowired
    private final AllergyService allergyService;

}
