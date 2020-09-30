package com.breakoutms.lfs.server.revenue.model;

import java.util.List;

import com.breakoutms.lfs.server.sales.model.SalesProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false) //TODO: IS THIS CLASS REALLY NECESSARY? CAN WE NOT JUST MOVE EVERYTHING TO 
//TODO: REVENUE_DTO?
public class RevenueEagerResponse extends RevenueDTO {

	private List<SalesProductDTO> salesProducts;
}
