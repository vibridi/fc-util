package com.vibridi.fcutil.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Player {

	private String offerer;
	private String role;
	private String name;
	private String team;
	private double cost;
	private double offer;
	private List<String> contenders;
	
	public Player(String offerer, String role, String name, String team, double cost, double offer) {
		this.offerer = offerer;
		this.role = role;
		this.name = name;
		this.team = team;
		this.cost = cost;
		this.offer = offer;
		this.contenders = new ArrayList<String>();
		this.contenders.add(offerer);
	}
	
	@Override
	public String toString() {
		return Arrays.stream(this.getClass().getDeclaredFields())
			.filter(field -> {
					String name = field.getName();
					switch(name) {
					case "offerer":
					case "contenders":
						return false;
					default:
						return true; 
					}
				})
			.map(field -> { 
					String name = field.getName();
					Object value;
					try {
						value = this.getClass().getMethod("get"+capitalize(name)).invoke(this);
					} catch (Exception e) {
						value = null;
					} 
					return name + "=[" + value + "]"; 
				})
			.collect(Collectors.joining(","));
	}
	
	private String capitalize(String s1) {
		return Character.toString(s1.charAt(0)).toUpperCase() + s1.substring(1);
	}
	
	public String getOfferer() {
		return offerer;
	}

	public void setOfferer(String offerer) {
		this.offerer = offerer;
	}
	
	public String getRole() {
		return role;
	}

	public String getName() {
		return name;
	}

	public String getTeam() {
		return team;
	}

	public double getCost() {
		return cost;
	}

	public double getOffer() {
		return offer;
	}

	public void addContender(String contender) {
		contenders.add(contender);
	}
	
	public List<String> getContenders() {
		return contenders;
	}

}
