

-- сессии пользователей
create table if not exists user_session
(
	session_id		varchar(128)	not null, 			-- идентификатор сессии пользователя
	user_id 		numeric(32)		not null,			-- идентификатор пользователя в Telegram
	user_name		varchar(128),						-- имя пользователя
	device_id		varchar(128),						-- идентификатор устройсва пользователя
	device_name		varchar(256),						-- имя устройства
	accept_code		numeric(16),						-- код подтверждения
	create_time		timestamp		not null,			-- время создания сессии
	try_count		numeric(2)		not null default 0,	-- количество неуспешниых попыток аутентификации
	update_time		timestamp							-- время последнего подключения
);
create unique index if not exists user_session_primary_key on user_session(session_id);
create unique index if not exists user_session_user_index on user_session(user_id, device_id);

-- книги пользователя
create table if not exists user_book
(
	user_id 			numeric(32)			not null,			-- идентификатор пользователя в Telegram
	book_source			varchar(128),							-- источник получения
	book_id				varchar(128)		not null,			-- идентификатор книги
	current_chapter		numeric(6)			not null default 0,	-- номер открытой главы книги
	current_position	double precision	not null default 0,	-- позиция в открытой главе книги
	update_time			timestamp,								-- время последнего открытия
	book_version		numeric(16)			not null default 0,	-- версия книги
	progress			numeric(3)			not null default 0  -- прогресс чтения книги (%)
);
create unique index if not exists user_book_primary_key on user_book(user_id, book_id);

-- книги
create table if not exists book
(
	book_id				varchar(128)	not null,			-- идентификатор книги
	chat_id				numeric(32)		not null,			-- идентификатор группы в Telegram
	file				bytea			not null,			-- содержимое файла
	book_version		numeric(16)		not null default 0	-- версия файла
);
create unique index if not exists book_primary_key on book(book_id);
create index if not exists book_chat_id_index on book(chat_id);

-- подписчики Telegram-группы
create table if not exists chat_subscriber
(
	chat_id				numeric(32)		not null,	-- идентификатор группы в Telegram
	chat_name           varchar(256)    not null,   -- имя Telegram-группы
	user_id				numeric(32)		not null	-- идентификатор пользователя в Telegram
);
create index if not exists chat_subscriber_chat_id_index on chat_subscriber(chat_id);
create index if not exists chat_subscriber_user_id_index on chat_subscriber(user_id, chat_name);

-- сообщения Telegram-группы
create table if not exists chat_message
(
	chat_id				numeric(32)		not null,	-- идентификатор группы в Telegram
	username			varchar(128),				-- имя Telegram-пользователя
	message_id			numeric(32)		not null,	-- идентификатор сообщения в группе Telegram
	category			varchar,					-- категория сообщения (хештег)
	message				jsonb			not null	-- сообщение в формате json
);
create unique index if not exists chat_message_primary_key on chat_message(chat_id, message_id);
