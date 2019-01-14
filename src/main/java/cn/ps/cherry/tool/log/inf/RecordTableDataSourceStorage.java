package cn.ps.cherry.tool.log.inf;

import javax.persistence.Column;
import javax.sql.DataSource;

import cn.ps.cherry.tool.log.persist.GenerationType;
import cn.ps.cherry.tool.log.persist.annotion.RecordTable;

public class RecordTableDataSourceStorage extends DataSourceStorage {
	private RecordTable recordTable;
	private DataSource dataSource;
	public RecordTableDataSourceStorage(RecordTable recordTable,DataSource dataSource) {
		this.recordTable = recordTable;
		this.dataSource=dataSource;
	}

	@Override
	public DataSource dataSource() {
		return dataSource;
	}

	@Override
	public String tableName() {
		return recordTable.table().name();
	}
	@Override
	public String[] columns() {
		Column[] columns=recordTable.columns();
		String[] strColumns=new String[columns.length];
		for (int i = 0; i < columns.length; i++) {
			strColumns[i]=columns[i].name();
		}
		return strColumns;
	}

	@Override
	public String keyName() {
		return recordTable.id();
	}

	@Override
	public GenerationType generationType() {
		return recordTable.generationType();
	}

}
