package org.team10424102.blackserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ChatRoomController {

	static final Logger logger = LoggerFactory.getLogger(ChatRoomController.class);

	@RequestMapping(value = "/chatroom", method = RequestMethod.GET)
	public ModelAndView enterChatRoom() {
		logger.info("a request to chatroom hit.");
		return new ModelAndView("chatroom");
	}

}
