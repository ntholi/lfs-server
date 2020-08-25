package com.breakoutms.lfs.server.sales.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class SalesEagerResponse extends SalesViewModel{

	private List<SalesProductViewModel> salesProducts;
}
