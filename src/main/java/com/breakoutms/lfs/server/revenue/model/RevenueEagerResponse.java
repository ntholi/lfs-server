package com.breakoutms.lfs.server.revenue.model;

import java.util.List;

import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RevenueEagerResponse extends RevenueViewModel {

	private List<SalesProductViewModel> salesProducts;
}
