package com.ajira.rover.ajirayaan.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Performs {
	@JsonProperty("collect-sample")
	private CollectSample collectSample;
	public CollectSample getCollectSample() {
		return collectSample;
	}
	public void setCollectSample(CollectSample collectSample) {
		this.collectSample = collectSample;
	}
}
