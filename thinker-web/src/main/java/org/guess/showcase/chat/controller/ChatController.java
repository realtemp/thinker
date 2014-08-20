package org.guess.showcase.chat.controller;

import org.guess.sys.util.UserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by rguess on 2014/8/19.
 */

@Controller
@RequestMapping("/chat/servlet3")
public class ChatController {

    private final static String indexView = "/chat/servlet3";

    private MsgPublisher msgPublisher = MsgPublisher.getInstance();

    private volatile Map<String, Queue<DeferredResult<String>>> usernameToDeferredResultMap = new ConcurrentHashMap();

    @RequestMapping
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView(indexView);
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "poll",method = RequestMethod.POST)
    public DeferredResult<String> poll(HttpServletRequest request) {

        String loginId = UserUtil.getCurrentUser().getLoginId();
        return msgPublisher.startAsync(loginId);
    }

    @RequestMapping(value = "send",method = RequestMethod.POST)
    @ResponseBody
    private void send(final HttpServletRequest req) {
        String sender = UserUtil.getCurrentUser().getLoginId();
        String receiver = req.getParameter("receiver");
        String msg = req.getParameter("msg");
        msgPublisher.send(receiver, sender, msg);
    }

}