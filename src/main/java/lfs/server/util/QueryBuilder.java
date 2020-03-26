package lfs.server.util;

public class QueryBuilder {
	
	private StringBuilder select = new StringBuilder();
	private StringBuilder join = new StringBuilder();
	private StringBuilder groupBy = new StringBuilder("group by");
	private StringBuilder orderBy = new StringBuilder("order by");
	private StringBuilder where = new StringBuilder("where");
	
	public QueryBuilder(String sql) {
		select(sql);
		where(sql);
		join(sql);
		groupBy(sql);
		orderBy(sql);
	}
	
	public QueryBuilder() {
	}
	
	public String build() {
		return getSelect() +" "
				+ getJoin() + " "
				+ getWhere() +" "
				+ getGroupBy() +" "
				+ getOrderBy();
	}
	
	public QueryBuilder select(String sql) {
		if(!sql.trim().toLowerCase().startsWith("select")) {
			select.append("select ");
		}
		String[] parts = sql.split("\\s+");
		int end = lastIndexOf(parts, "from");
		int i = 0;
		for(; i < end; i++) {
			select.append(parts[i]).append(" ");
		}
		select.append(parts[i]).append(" ").append(parts[++i]);
		return this;
	}
	
	public QueryBuilder where(String sql) {
		if(inSubquery(sql, "where")) {
			return this;
		}
		if(!sql.toLowerCase().contains("where")) {
			sql = "where "+ sql;
		}
		String[] parts = sql.split("\\s+");
		if(where.length() > 5) {
			where.append(" and");
		}
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].equalsIgnoreCase("where")) {
				i += 1;
				while(i < parts.length && !isKeyword(parts[i])) {
					if(parts[i] != ",") {
						where.append(" ").append(parts[i]);
					}
					i++;
				}
			}
		}
		return this;
	}
	
	public QueryBuilder groupBy(String sql) {
		if(!sql.toLowerCase().contains("group by")) {
			sql = "group by "+ sql;
		}
		String[] parts = sql.split("\\s+");
		if(groupBy.length() > 8) {
			groupBy.append(",");
		}
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].equalsIgnoreCase("group")) {
				i += 2;
				while(i < parts.length && !isKeyword(parts[i])) {
					if(parts[i] != ",") {
						groupBy.append(" ").append(parts[i]);
					}
					i++;
				}
			}
		}
		return this;
	}
	
	public QueryBuilder orderBy(String sql) {
		if(!sql.toLowerCase().contains("order by")) {
			sql = "order by "+ sql;
		}
		String[] parts = sql.split("\\s+");
		if(orderBy.length() > 8) {
			orderBy.append(",");
		}
		for(int i = 0; i < parts.length; i++) {
			if(parts[i].equalsIgnoreCase("order")) {
				i += 2;
				while(i < parts.length && !isKeyword(parts[i])) {
					if(parts[i] != ",") {
						orderBy.append(" ").append(parts[i]);
					}
					i++;
				}
			}
		}
		return this;
	}

	public QueryBuilder join(String sql) {
		String[] parts = sql.split("\\s+");
		for(int i = 0; i < parts.length; i++) {
			if(isJoin(parts, i)) {
				while(i < parts.length && isKeyword(parts[i])) {
					join.append(parts[i]).append(" ");
					i++;
				}
				while(i < parts.length && !isKeyword(parts[i])) {
					join.append(parts[i]).append(" ");
					if(isKeyword(parts[i])) {
						break;
					}
					i++;
				}
				--i;
			}
		}
		return this;
	}

	private boolean isJoin(String[] parts, int index) {
		if(index >= (parts.length+1)) {
			return false;
		}
		String joinType = parts[index];
		if(joinType.equalsIgnoreCase("inner")) {
			return parts[index+1].equalsIgnoreCase("join");
		}
		else if(joinType.equalsIgnoreCase("left")) {
			return parts[index+1].equalsIgnoreCase("join");
		}
		else if(joinType.equalsIgnoreCase("right")) {
			return parts[index+1].equalsIgnoreCase("join");
		}
		else if(joinType.equalsIgnoreCase("full")) {
			return parts[index+1].equalsIgnoreCase("join");
		}
		return parts[index].equalsIgnoreCase("join");
	}

	private boolean inSubquery(String sql, String keyword) {
		sql = sql.toLowerCase();
		if(sql.replaceAll("\\s+", "").contains("(select")) {
			String[] parts = sql.split("\\s+");
			for (int i = 0; i < parts.length; i++) {
				if(parts[i].startsWith("(") 
						&& (parts[i].equals("(select") || ((i+1 < parts.length-1) && parts[i+1].equals("select")))) {
					for(int x = i; x < parts.length; x++, i++) {
						if(parts[x].startsWith(")")) {
							break;
						}
						if(parts[x].equals(keyword)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private int lastIndexOf(String[] parts, String string) {
		int index = -1;
		for(int i = 0; i< parts.length; i++) {
			if(parts[i].equalsIgnoreCase(string)) {
				index = i;
			}
		}
		return index;
	}
	
	private boolean isKeyword(String string) {
		return string.equalsIgnoreCase("select")
				|| string.equalsIgnoreCase("from")
				|| string.equalsIgnoreCase("inner")
				|| string.equalsIgnoreCase("left")
				|| string.equalsIgnoreCase("right")
				|| string.equalsIgnoreCase("full")
				|| string.equalsIgnoreCase("join")
				|| string.equalsIgnoreCase("where")
				|| string.equalsIgnoreCase("group")
				|| string.equalsIgnoreCase("order");
	}
	
	public String getSelect() {
		return select.toString();
	}
	
	public String getWhere() {
		String str = where.toString();
		return str.length() > 5? str: "";
	}
	
	public String getJoin() {
		return join.toString();
	}
	
	public String getGroupBy() {
		String str = groupBy.toString();
		return str.length() > 8? str: "";
	}
	
	public String getOrderBy() {
		String str =  orderBy.toString();
		return str.length() > 8? str: "";
	}
	
	@Override
	public String toString() {
		return build();
	}
}