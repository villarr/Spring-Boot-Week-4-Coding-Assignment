package com.promineotech.jeep.entity;

import java.math.BigDecimal;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Jeeps implements Comparable<Jeeps> {
	
		  private Long modelPK;
		  private JeepModel modelId;
		  private String trimLevel;
		  private int numDoors;
		  private int wheelSize;
		  private BigDecimal basePrice;

		@JsonIgnore
		public Long getModelPk() {
			return modelPK;
		}

		@Override
		public int compareTo(Jeeps that) {
	 // @formatter:off
		return Comparator.comparing(Jeeps::getModelId)
				.thenComparing(Jeeps::getTrimLevel)
				.thenComparing(Jeeps::getNumDoors)
				.compare(this, that);
	// @formatter:on

		}

	}

