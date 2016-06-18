package com.shawn.server.provider.manage.dao.redis;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Repository;

import com.shawn.server.core.util.DataConverUtil;
import com.shawn.server.core.util.JsonUtil;
import com.shawn.server.entity.pojo.User;

@Repository(value = "userRedisDao")
public class UserRedisDao extends RedisGeneratorDao<String, User> {

	private static final String USER_KEY_PERFIX = "user_id_";
	private static final String USER_KEY_SEQUENCE = USER_KEY_PERFIX + "sequence";

	/**
	 * 取得User的ID序列
	 *
	 * @return
	 */
	public synchronized int nextSequence() {
		int seq = redisTemplate.execute(new RedisCallback<Integer>() {

			@Override
			public Integer doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_SEQUENCE);
				byte[] value = connection.get(key);
				if (value == null) {
					if (!initSequece()) {
						return 0;
					}
					return 1;
				}
				int seq = DataConverUtil.string2int(serializer.deserialize(value));
				sequece2next(seq);
				return seq;
			}

		});
		return seq;
	}

	/**
	 * 初始化User的ID序列
	 *
	 * @return
	 */
	private boolean initSequece() {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_SEQUENCE);
				byte[] name = serializer.serialize(1 + "");
				return connection.setNX(key, name);
			}
		});

		return result;
	}

	/**
	 * User的ID序列自增
	 *
	 * @param seq
	 * @return
	 */
	private boolean sequece2next(final int seq) {
		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_SEQUENCE);
				int next = seq + 1;
				byte[] name = serializer.serialize(next + "");
				connection.set(key, name);
				return true;
			}

		});

		return result;
	}

	/**
	 * GET
	 *
	 * @param userId
	 * @return
	 */
	public User get(final Integer userId) {
		if (userId == null) {
			throw new NullPointerException("userID is null;");
		}

		User user = redisTemplate.execute(new RedisCallback<User>() {

			@Override
			public User doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_PERFIX + userId);
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				String valueStr = serializer.deserialize(value);
				return JsonUtil.JsonStr2Object(valueStr, User.class);
			}
		});

		return user;
	}

	/**
	 * ADD
	 *
	 * @param user
	 * @return
	 */
	public boolean add(final User user) {
		if (user.getUserId() == null) {
			throw new NullPointerException("userID is null;");
		}

		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_PERFIX + user.getUserId());
				byte[] name = serializer.serialize(JsonUtil.Object2JsonStr(user));
				return connection.setNX(key, name);
			}
		});

		return result;
	}

	/**
	 * DELETE
	 *
	 * @param userId
	 */
	public void delete(final Integer userId) {
		if (userId == null) {
			throw new NullPointerException("userID is null;");
		}

		redisTemplate.delete(USER_KEY_PERFIX + userId);
	}

	/**
	 * UPDATE
	 *
	 * @param user
	 * @return
	 */
	public boolean update(final User user) {
		if (user.getUserId() == null) {
			throw new NullPointerException("userID is null;");
		}

		if (get(user.getUserId()) == null) {
			throw new NullPointerException("data is not exist");
		}

		boolean result = redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<String> serializer = getRedisSerializer();
				byte[] key = serializer.serialize(USER_KEY_PERFIX + user.getUserId());
				byte[] name = serializer.serialize(JsonUtil.Object2JsonStr(user));
				connection.set(key, name);
				return true;
			}

		});

		return result;
	}

}
