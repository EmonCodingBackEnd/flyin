package com.coding.flyin.cmp.exception;

import lombok.Getter;

@Getter
public enum AppStatus {
    SUCCESS(0, "一切 ok - 正确执行后的返回"),

    U0001(10001, "用户端错误 - 一级宏观错误码"),
    U0100(10100, "用户注册错误 - 二级宏观错误码"),
    U0101(10101, "用户未同意隐私协议"),
    U0102(10102, "注册国家或地区受限"),
    U0110(10110, "用户名校验失败"),
    U0111(10111, "用户名已存在"),
    U0112(10112, "用户名包含敏感词"),
    U0113(10113, "用户名包含特殊字符"),
    U0120(10120, "密码校验失败"),
    U0121(10121, "密码长度不够"),
    U0122(10122, "密码强度不够"),
    U0130(10130, "校验码输入错误"),
    U0131(10131, "短信校验码输入错误"),
    U0132(10132, "邮件校验码输入错误"),
    U0133(10133, "语音校验码输入错误"),
    U0140(10140, "用户证件异常"),
    U0141(10141, "用户证件类型未选择"),
    U0142(10142, "大陆身份证编号校验非法"),
    U0143(10143, "护照编号校验非法"),
    U0144(10144, "军官证编号校验非法"),
    U0150(10150, "用户基本信息校验失败"),
    U0151(10151, "手机格式校验失败"),
    U0152(10152, "地址格式校验失败"),
    U0153(10153, "邮箱格式校验失败"),
    U0200(10200, "用户登陆异常 - 二级宏观错误码"),
    U0201(10201, "用户账户不存在"),
    U0202(10202, "用户账户被冻结"),
    U0203(10203, "用户账户已作废"),
    U0204(10204, "不支持的认证方式"),
    U0210(10210, "用户密码错误"),
    U0211(10211, "用户输入密码次数超限"),
    U0220(10220, "用户身份校验失败"),
    U0221(10221, "用户指纹识别失败"),
    U0222(10222, "用户面容识别失败"),
    U0223(10223, "用户未获得第三方登陆授权"),
    U0224(10224, "用户的认证账号不具有唯一性"),
    U0225(10225, "用户信息加载失败"),
    U0230(10230, "用户登陆已过期"),
    U0240(10240, "用户验证码错误"),
    U0241(10241, "用户验证码尝试次数超限"),
    U0300(10300, "访问权限异常 - 二级宏观错误码"),
    U0301(10301, "访问未授权"),
    U0302(10302, "正在授权中"),
    U0303(10303, "用户授权申请被拒绝"),
    U0310(10310, "因访问对象隐私设置被拦截"),
    U0311(10311, "授权已过期"),
    U0312(10312, "无权限使用 API"),
    U0320(10320, "用户访问被拦截"),
    U0321(10321, "黑名单用户"),
    U0322(10322, "账号被冻结"),
    U0323(10323, "非法 IP 地址"),
    U0324(10324, "网关访问受限"),
    U0325(10325, "地域黑名单"),
    U0330(10330, "服务已欠费"),
    U0340(10340, "用户签名异常"),
    U0341(10341, "RSA 签名错误"),
    U0400(10400, "用户请求参数错误 - 二级宏观错误码"),
    U0401(10401, "包含非法恶意跳转链接"),
    U0402(10402, "无效的用户输入"),
    U0410(10410, "请求必填参数为空"),
    U0411(10411, "用户订单号为空"),
    U0412(10412, "订购数量为空"),
    U0413(10413, "缺少时间戳参数"),
    U0414(10414, "非法的时间戳参数"),
    U0420(10420, "请求参数值超出允许的范围"),
    U0421(10421, "参数格式不匹配"),
    U0422(10422, "地址不在服务范围"),
    U0423(10423, "时间不在服务范围"),
    U0424(10424, "金额超出限制"),
    U0425(10425, "数量超出限制"),
    U0426(10426, "请求批量处理总个数超出限制"),
    U0427(10427, "请求 JSON 解析失败"),
    U0430(10430, "用户输入内容非法"),
    U0431(10431, "包含违禁敏感词"),
    U0432(10432, "图片包含违禁信息"),
    U0433(10433, "文件侵犯版权"),
    U0440(10440, "用户操作异常"),
    U0441(10441, "用户支付超时"),
    U0442(10442, "确认订单超时"),
    U0443(10443, "订单已关闭"),
    U0500(10500, "用户请求服务异常 - 二级宏观错误码"),
    U0501(10501, "请求次数超出限制"),
    U0502(10502, "请求并发数超出限制"),
    U0503(10503, "用户操作请等待"),
    U0504(10504, "WebSocket 连接异常"),
    U0505(10505, "WebSocket 连接断开"),
    U0506(10506, "用户重复请求"),
    U0507(10507, "用户请求太过频繁"),
    U0600(10600, "用户资源异常 - 二级宏观错误码"),
    U0601(10601, "账户余额不足"),
    U0602(10602, "用户磁盘空间不足"),
    U0603(10603, "用户内存空间不足"),
    U0604(10604, "用户 OSS 容量不足"),
    U0605(10605, "用户配额已用光"),
    U0700(10700, "用户上传文件异常 - 二级宏观错误码"),
    U0701(10701, "用户上传文件类型不匹配"),
    U0702(10702, "用户上传文件太大"),
    U0703(10703, "用户上传图片太大"),
    U0704(10704, "用户上传视频太大"),
    U0705(10705, "用户上传压缩文件太大"),
    U0800(10800, "用户当前版本异常 - 二级宏观错误码"),
    U0801(10801, "用户安装版本与系统不匹配"),
    U0802(10802, "用户安装版本过低"),
    U0803(10803, "用户安装版本过高"),
    U0804(10804, "用户安装版本已过期"),
    U0805(10805, "用户 API 请求版本不匹配"),
    U0806(10806, "用户 API 请求版本过高"),
    U0807(10807, "用户 API 请求版本过低"),
    U0900(10900, "用户隐私未授权 - 二级宏观错误码"),
    U0901(10901, "用户隐私未签署"),
    U0902(10902, "用户摄像头未授权"),
    U0904(10904, "用户图片库未授权"),
    U0905(10905, "用户文件未授权"),
    U0906(10906, "用户位置信息未授权"),
    U0907(10907, "用户通讯录未授权"),
    U1000(11000, "用户设备异常 - 二级宏观错误码"),
    U1001(11001, "用户相机异常"),
    U1002(11002, "用户麦克风异常"),
    U1003(11003, "用户听筒异常"),
    U1004(11004, "用户扬声器异常"),
    U1005(11005, "用户 GPS 定位异常"),

    S0001(20001, "系统执行出错 - 一级宏观错误码"),
    S0100(20100, "系统执行超时 - 二级宏观错误码"),
    S0101(20101, "系统订单处理超时"),
    S0200(20200, "系统容灾功能被触发 - 二级宏观错误码"),
    S0210(20210, "系统限流"),
    S0220(20220, "系统功能降级"),
    S0300(20300, "系统资源异常 - 二级宏观错误码"),
    S0310(20310, "系统资源耗尽"),
    S0311(20311, "系统磁盘空间耗尽"),
    S0312(20312, "系统内存耗尽"),
    S0313(20313, "文件句柄耗尽"),
    S0314(20314, "系统连接池耗尽"),
    S0315(20315, "系统线程池耗尽"),
    S0320(20320, "系统资源访问异常"),
    S0321(20321, "系统读取磁盘文件失败"),

    T0001(20001, "调用第三方服务出错 - 一级宏观错误码"),
    T0100(20100, "中间件服务出错 - 二级宏观错误码"),
    T0110(20110, "RPC 服务出错"),
    T0111(20111, "RPC 服务未找到"),
    T0112(20112, "RPC 服务未注册"),
    T0113(20113, "接口不存在"),
    T0120(20120, "消息服务出错"),
    T0121(20121, "消息投递出错"),
    T0122(20122, "消息消费出错"),
    T0123(20123, "消息订阅出错"),
    T0124(20124, "消息分组未查到"),
    T0130(20130, "缓存服务出错"),
    T0131(20131, "key 长度超过限制"),
    T0132(20132, "value 长度超过限制"),
    T0133(20133, "存储容量已满"),
    T0134(20134, "不支持的数据格式"),
    T0140(20140, "配置服务出错"),
    T0150(20150, "网络资源服务出错"),
    T0151(20151, "VPN 服务出错"),
    T0152(20152, "CDN 服务出错"),
    T0153(20153, "域名解析服务出错"),
    T0154(20154, "网关服务出错"),
    T0200(20200, "第三方系统执行超时 - 二级宏观错误码"),
    T0210(20210, "RPC 执行超时"),
    T0220(20220, "消息投递超时"),
    T0230(20230, "缓存服务超时"),
    T0240(20240, "配置服务超时"),
    T0250(20250, "数据库服务超时"),
    T0300(20300, "数据库服务出错 - 二级宏观错误码"),
    T0311(20311, "表不存在"),
    T0312(20312, "列不存在"),
    T0321(20321, "多表关联中存在多个相同名称的列"),
    T0331(20331, "数据库死锁"),
    T0341(20341, "主键冲突"),
    T0400(20400, "第三方容灾系统被触发 - 二级宏观错误码"),
    T0401(20401, "第三方系统限流"),
    T0402(20402, "第三方功能降级"),
    T0500(20500, "通知服务出错 - 二级宏观错误码"),
    T0501(20501, "短信提醒服务失败"),
    T0502(20502, "语音提醒服务失败"),
    T0503(20503, "邮件提醒服务失败"),
    ;

    /**
     * 错误码规范：<br>
     *
     * <ol>
     *   <li><span color="red">【强制】</span>错误码的制定规则：快速溯源、简单易记、沟通标准化。
     *   <li><span color="red">【强制】</span>错误码不体现版本号和错误等级信息。
     *   <li><span color="red">【强制】</span>全部正常，但不得不填充错误码时返回五个零：00000。
     *   <li><span color="red">【强制】</span>错误码为字符串类型，共5位，分成两个部分：错误产生来源 + 四位数字编号。
     *       <p>说明：错误产生来源分为U/S/T，U表示错误来源于用户，比如参数错误；S表示错误来源于当前系统，往往是业务逻辑出错；<br>
     *       T表示错误来源于第三方服务，比如CDN服务出错； 四位数字编号从0001到9999，大类之间的步长间距预留100。
     *   <li><span color="red">【强制】</span>编号不与公司业务架构、更不与组织架构挂钩，一切与平台先到先申请的原则进行，审批生效，编号即被永久固定。
     *   <li><span color="red">【强制】</span>错误码使用者避免随意定义新的错误码。
     *   <li><span color="red">【强制】</span>错误码不能直接输出给用户作为提示信息使用。
     *       <p>说明：堆栈（stack_trace）、错误信息（error_message）、错误码（error_code）、<br>
     *       提示信息（user_tip）是一个有效关联并相互转义的和谐整体，但是请勿越俎代庖。
     *   <li><span color="yellow">【推荐】</span>错误码之外的业务独特信息由error_message来承载，而不是让错误码本身涵盖过多具体业务属性。
     *   <li><span color="yellow">【推荐】</span>在获取第三方服务错误码时，向上抛出允许本系统转义，由T转为S，并且在错误信息上带有原有的第三方错误码。
     *   <li><span color="green">【参考】</span>错误码分为一级宏观错误码、二级宏观错误码、三级宏观错误码。
     *       <p>说明：在无法更加具体确定的错误场景中，可以直接使用一级宏观错误码。
     *       <p>正例：调用第三方服务出错是一级、中间件错误是二级、消息服务出错是三级。
     * </ol>
     */
    private final Integer errorCode;

    private final String errorMessage;

    AppStatus(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
