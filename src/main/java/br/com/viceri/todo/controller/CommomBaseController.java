package br.com.viceri.todo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class CommomBaseController {

	@Autowired
	private ModelMapper modelMapper;
	
	public ModelMapper getModelMapper() {
		return modelMapper;
	}
}
