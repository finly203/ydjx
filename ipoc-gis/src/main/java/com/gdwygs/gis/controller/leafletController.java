package com.gdwygs.gis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class leafletController {

	@RequestMapping(value = "/leaflet_demo", method = RequestMethod.GET)
	public String leaflet_demo(ModelMap model) {
		return "gis/leaflet_demo";
	}
}
