package com.redhat.request.timer.controller;

import io.swagger.annotations.ApiOperation;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("request-timer/v1/api")
public class TimerController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@CrossOrigin
	@ApiOperation("Request Timer")
	@RequestMapping(value = "timer/fixed/{time}", method = RequestMethod.GET)
	public ResponseEntity<String> fixedTimer(
			@PathVariable(required = true) long time) {

		log.debug("Starting {} fixed timer...", time);

		ResponseEntity<String> response;
		try {
			Thread.sleep(time);
			response = new ResponseEntity<String>("Finish " + time + " ms.",
					HttpStatus.OK);
		} catch (InterruptedException e) {
			response = new ResponseEntity<String>(e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.debug("Finished {} fixed timer.", time);

		return response;
	}

	@CrossOrigin
	@ApiOperation("Request Timer")
	@RequestMapping(value = "timer/rand/{min}/{max}", method = RequestMethod.GET)
	public ResponseEntity<String> randTimer(
			@PathVariable(required = true) long min, @PathVariable(required=true) long max) {
		
		Random r = new Random();
		long randomTime = r.longs(min, max).findFirst().getAsLong();

		log.debug("Starting {} random timer...", randomTime);

		ResponseEntity<String> response;
		try {
			Thread.sleep(randomTime);
			response = new ResponseEntity<String>("Finish " + randomTime + " ms.",
					HttpStatus.OK);
		} catch (InterruptedException e) {
			response = new ResponseEntity<String>(e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		log.debug("Finished {} random timer.", randomTime);

		return response;
	}

}
