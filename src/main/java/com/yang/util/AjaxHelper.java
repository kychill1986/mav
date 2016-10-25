package com.yang.util;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AjaxHelper {

        public static final String SUCCESS = "success";
        public static final String STATUS = "status";
        public static final String MESSAGE = "message";
		
		// AJAX输出，返回null
		public static String ajax(String content, String type, HttpServletResponse response) {
			try {
				response.setContentType(type + ";charset=UTF-8");
				response.setHeader("Pragma", "No-cache");
				response.setHeader("Cache-Control", "no-cache");
				response.setDateHeader("Expires", 0);
				response.getWriter().write(content);
				response.getWriter().flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		// AJAX输出，返回null
		public static String ajaxText(String text, HttpServletResponse response) {
			return ajax(text, "text/plain", response);
		}

		// AJAX输出HTML，返回null
		public static String ajaxHtml(String html, HttpServletResponse response) {
			return ajax(html, "text/html", response);
		}

		// AJAX输出XML，返回null
		public static String ajaxXml(String xml, HttpServletResponse response) {
			return ajax(xml, "text/xml", response);
		}

		// 根据字符串输出JSON，返回null
		public static String ajaxJson(String jsonString,
				HttpServletResponse response) {
			return ajax(jsonString, "text/html", response);
		}
        // 输出JSON成功消息，返回null
        public static String ajaxJsonSuccessMessage(String message, HttpServletResponse response) {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put(STATUS, SUCCESS);
            jsonMap.put(MESSAGE, message);
            Gson gson = new Gson();
            return ajax(gson.toJson(jsonMap).toString(), "text/html", response);
        }

}
