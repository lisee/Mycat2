package io.mycat.mycat2;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

import io.mycat.proxy.BufferPool;

/**
 * 后端MySQL连接
 * 
 * @author wuzhihui
 *
 */
public class MySQLSession extends AbstractMySQLSession {
	private String database;
	
	/**
	 * 当前缓存的 mysqlSession 所属的mysql-replica 的名称。用于快速判断当前连接是否可以被复用
	 */
	private String currBackendCachedName;
	/**
	 * 当前所从属的mycat sesssion
	 */
	private MycatSession mycatSession;

	public MySQLSession(BufferPool bufferPool, Selector selector, SocketChannel channel) throws IOException {
		super(bufferPool, selector, channel, SelectionKey.OP_CONNECT);
	}

	public MycatSession getMycatSession() {
		return mycatSession;
	}

	public void bind2MycatSession(MycatSession mycatSession) {
		this.useSharedBuffer(mycatSession.getProxyBuffer());
		this.mycatSession = mycatSession;
	}

	public void unbindMycatSession() {
		this.useSharedBuffer(null);
		this.mycatSession = null;

	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public void setMycatSession(MycatSession mycatSession) {
		this.mycatSession = mycatSession;
	}

	@Override
	protected void doTakeReadOwner() {
		this.getMycatSession().takeOwner(SelectionKey.OP_READ);
		
	}

	public String getCurrBackendCachedName() {
		return currBackendCachedName;
	}

	public void setCurrBackendCachedName(String currBackendCachedName) {
		this.currBackendCachedName = currBackendCachedName;
	}



}
