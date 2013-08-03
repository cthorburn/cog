package com.trabajo.engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;

public class EngineStatus implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean ok = true;
	private List<Msg> msgs = new ArrayList<>();
	private List<JsonException> exceptions = new ArrayList<>();

	@Expose(serialize = false, deserialize = false)
	private List<TaskDueDate> taskScheduling = new ArrayList<>();

	@Expose(serialize = false, deserialize = false)
	private transient Object result;

	private Serializable jsonResult;

	public static class Msg implements Serializable {

		private static final long serialVersionUID = 1L;

		public String sev;
		public String txt;
		public boolean tech = false;

		public Msg(String sev, String txt) {
			this.sev = sev;
			this.txt = txt;
		}

		public Msg(String txt) {
			this.sev = "TECH";
			this.txt = txt;
			this.tech = true;
		}
	}

	private void addMsg(String sev, String txt) {
		msgs.add(new Msg(sev, txt));
	}

	private void addMsgTech(String txt) {
		msgs.add(new Msg(txt));
	}

	public String toJSON() {
		return new Gson().toJson(this);
	}

	public void setResult(Object result) {
		this.result = result;

	}

	public Object getResult() {
		return result;
	}

	public void error(String txt, Logger logger, Exception e) {
		setOk(false);
		addMsgTech(txt);
		logger.error(txt, e);
	}

	public void error(String txt, Logger logger) {
		setOk(false);
		addMsgTech(txt);
		logger.error(txt);
	}

	public void warning(String txt, Logger logger, Exception e) {
		addMsg("WARNING", txt);
		logger.warn(txt);
	}

	public void error(String txt) {
		setOk(false);
		addMsgTech(txt);
	}

	public void info(String txt, Logger logger) {
		addMsg("INFO", txt);
		logger.error(txt);
	}

	public void info(String txt) {
		addMsg("INFO", txt);
	}

	public void exception(String exName, String detail, Logger logger, Exception e) {
		exceptions.add(new JsonException(exName, detail));
		setOk(false);
		addMsgTech(detail);
		logger.error(detail, e);
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public Object getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(Serializable jsonResult) {
		this.jsonResult = jsonResult;
	}

	public void exception(String exName, String detail) {
		exceptions.add(new JsonException(exName, detail));
		setOk(false);
	}

	public void deferMessages(DeferredMessages dm) {
		for (Msg msg : msgs) {
			dm.defer(msg);
		}
		msgs.clear();
		for (JsonException e : exceptions) {
			dm.defer(e.toMsg());
		}
		exceptions.clear();
	}

	public void setMessages(List<Msg> list) {
		this.msgs = list;

	}

	public String toJSONAddOK() {
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(this);
		jsonElement.getAsJsonObject().addProperty("ok", true);
		String s = gson.toJson(jsonElement);
		return s;
	}

	public List<TaskDueDate> getTaskScheduling() {
		return taskScheduling;
	}
}
