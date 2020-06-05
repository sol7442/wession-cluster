package com.wowsanta.wession.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.wowsanta.logger.LOG;
import com.wowsanta.wession.cluster.ClusterClient;
import com.wowsanta.wession.cluster.ClusterNode;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.session.Wession;

public class SyncManager {
	private static SyncManager instance = null;

	public static SyncManager getInstance() {
		if (instance == null) {
			instance = new SyncManager();
		}
		return instance;
	}

	int coreSize = 2;
	private transient boolean initialized = false;
	ExecutorService syncService = Executors.newFixedThreadPool(coreSize);
	BlockingQueue<Wession> queue = null;

	public boolean initialize() {
		try {
			if (!initialized) {
				coreSize = Runtime.getRuntime().availableProcessors();
				initialized = true;
			}
		} catch (Exception e) {
			LOG.system().error(e.getMessage(), e);
			initialized = false;
		} finally {
			LOG.system().info("initialized : {}-{} ", this.getClass().getName(), initialized);
		}
		return initialized;
	}

	public int sync(ClusterNode node) {
		int sync_count = 0;

		queue = new ArrayBlockingQueue<Wession>(CoreManager.getInstance().size() * 2);
		queue.addAll(CoreManager.getInstance().values());

		long start_time = System.currentTimeMillis();
		List<Future<Integer>> futer_list = new ArrayList<>();
		for (int i = 0; i < coreSize; i++) {
			Future<Integer> future = syncService.submit(new SyncWorker(node));
			futer_list.add(future);
		}

		try {
			for (Future<Integer> future : futer_list) {
				sync_count += future.get().intValue();
			}
		} catch (InterruptedException | ExecutionException e) {
			LOG.application().error(e.getMessage(), e);
		}
		long end_time = System.currentTimeMillis();
		LOG.system().info("{} : {} , {}",node.getName(), sync_count, (end_time - start_time));
		
		return sync_count;
	}

	public class SyncWorker implements Callable<Integer> {
		ClusterNode node;

		public SyncWorker(ClusterNode node) {
			this.node = node;
		}

		@Override
		public Integer call() throws Exception {
			int sync_count = 0;
			try (ClusterClient client = new ClusterClient(node.getAddress(), node.getPort())) {
				client.connect();

				Wession wession = null;
				while ((wession = queue.poll()) != null) {
					client.write(new CreateMessage(wession));
					sync_count++;
				}
			} catch (Exception e) {
				LOG.application().error(e.getMessage(), e);
			} finally {
				LOG.application().info("{} : {}", node.getName(), sync_count);
			}
			return sync_count;
		}
	}
}
