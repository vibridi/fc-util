package com.vibridi.fcutil.model;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Player {

	private String offerer;
	private String role;
	private String name;
	private String team;
	private double cost;
	private double offer;
	private boolean sold;
	
	public Player(String offerer, String role, String name, String team, double cost, double offer) {
		this.offerer = offerer;
		this.role = role;
		this.name = name;
		this.team = team;
		this.cost = cost;
		this.offer = offer;
		this.setSold(false);
	}
	
	@Override
	public String toString() {
		return Arrays.stream(this.getClass().getDeclaredFields())
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

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}


}
