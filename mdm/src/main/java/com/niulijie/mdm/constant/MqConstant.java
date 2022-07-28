package com.niulijie.mdm.constant;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.sync.data.demo.constant
 * @email zhoupengbing@telecomyt.com.cn
 * @description 消息队列常量信息
 * @createTime 2020年05月21日 11:38:00
 * @Version v1.0
 */
public class MqConstant {

    /**
     * 通话组-交换机[主题交换机]
     */
    public static final String VIDEO_DELAY_EXCHANGE = "video.delay.exchange";

    /**
     * 通话组消费队列
     */
    public static  final String VIDEO_TOP_SCHEDULE_QUEUE = "video.top.schedule.queue";

    /**
     * 通话组队列绑定的key
     */
    public static  final String VIDEO_TOP_SCHEDULE_KEY = "video.top.schedule.#";

    /**
     * 通话组-交换机[主题交换机]
     */
    public static final String VIDEO_BLACKLIST_EXCHANGE = "video.blacklist.exchange";

    /**
     * 通话组消费队列
     */
    public static  final String VIDEO_BLACKLIST_QUEUE = "video.blacklist.queue";

    /**
     * 通话组队列绑定的key
     */
    public static  final String VIDEO_BLACKLIST_KEY = "video.blacklist.#";
}
