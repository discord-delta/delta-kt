package delta.entity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class UnavailableGuildData(
    /**
     * The user's [Snowflake ID](https://discord.dev/reference#snowflakes). Serialized as a String.
     */
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    /**
     * Whether the guild is unavailable. False in the GuildLeaveEvent when the user leaves or is kicked.
     */
    val unavailable: Boolean
)

/**
 * Represents a serializable implementation of the [Discord Guild](https://discord.dev/resources/guild#guild-object) structure.
 */
@Serializable
data class GuildData(
    /**
     * The user's [Snowflake ID](https://discord.dev/reference#snowflakes). Serialized as a String.
     */
    @Serializable(with = SnowflakeSerializer::class)
    val id: Long,
    /**
     * The name of the guild.
     */
    val name: String,
    /**
     * The guild's [icon hash](https://discord.dev/reference#image-formatting).
     */
    @SerialName("icon")
    val iconHash: String? = null,
    /**
     * The guild's [splash hash](https://discord.dev/reference#image-formatting).
     */
    @SerialName("splash")
    val splashHash: String? = null,
    /**
     * The guild's [discovery splash hash](https://discord.dev/reference#image-formatting).
     */
    @SerialName("discovery_splash")
    val discoverySplashHash: String? = null,
    /**
     * Whether the current user owns the guild.
     */
    @SerialName("owner")
    val isOwner: Boolean = false,
    /**
     * The [Snowflake ID](https://discord.dev/reference#snowflakes) of the guild owner. Serialized as a String.
     */
    @SerialName("owner_id") @Serializable(with = SnowflakeSerializer::class)
    val ownerId: Long? = null,
    /**
     * The voice region ID of the guild.
     */
    val region: String,
    /**
     * The [Snowflake ID](https://discord.dev/reference#snowflakes) of the guild's AFK channel.
     */
    @SerialName("afk_channel_id") @Serializable(with = SnowflakeSerializer::class)
    val afkChannelId: Long? = null,
    /**
     * The guild's AFK timeout in seconds.
     */
    @SerialName("afk_timeout")
    val afkTimeout: Int,
    /**
     * Whether the guild's widget is enabled.
     */
    val widgetEnabled: Boolean? = null,
    @SerialName("widget_channel_id") @Serializable(with = SnowflakeSerializer::class)
    /**
     * The [Snowflake ID](https://discord.dev/reference#snowflakes) of the guild's widget channel.
     */
    val widgetChannel: Long? = null,
    @SerialName("verification_level")
    /**
     * The verification level of the guild.
     */
    val verificationLevel: VerificationLevel,
    /**
     * The guild's features.
     */
    val features: List<GuildFeature>,
    /**
     * The guild's required MFA level.
     */
    @SerialName("mfa_level")
    val mfaLevel: MfaLevel,
    /**
     * The [Snowflake ID](https://discord.dev/reference#snowflakes) of the application that owns the guild
     * if it is owned by a bot.
     */
    @SerialName("application_id") @Serializable(with = SnowflakeSerializer::class)
    val applicationId: Long?,
    /**
     * The list of guild emojis.
     */
    val emojis: List<EmojiData>,
    @SerialName("joined_at")
    /**
     * The timestamp the guild was joined at.
     */
    val joinedAt: String? = null
)

@Serializable
data class GuildMemberData(
    val user: UserData? = null,
    @SerialName("nick")
    val nickname: String? = null,
    val roles: List<Long>,
    @SerialName("joined_at")
    val joinedAt: String,
    @SerialName("premium_since")
    val premiumSince: String? = null,
    val deaf: Boolean,
    val mute: Boolean,
    @SerialName("pending")
    val pending: Boolean? = null
)

@Serializable(with = VerificationLevel.Serializer::class)
sealed class VerificationLevel(val id: Int) {

    object None : VerificationLevel(0)
    object Low : VerificationLevel(1)
    object Medium : VerificationLevel(2)
    object High : VerificationLevel(3)
    object VeryHigh : VerificationLevel(4)

    class Unknown(id: Int) : VerificationLevel(id)

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<VerificationLevel> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.VerificationLevel", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: VerificationLevel) = encoder.encodeInt(value.id)

        override fun deserialize(decoder: Decoder): VerificationLevel = when(val id = decoder.decodeInt()) {
            0 -> None
            1 -> Low
            2 -> Medium
            3 -> High
            4 -> VeryHigh
            else -> Unknown(id)
        }
    }
}

@Serializable(with = MessageNotificationLevel.Serializer::class)
sealed class MessageNotificationLevel(val id: Int) {

    object AllMessages : MessageNotificationLevel(0)
    object OnlyMentions : MessageNotificationLevel(1)

    class Unknown(id: Int) : MessageNotificationLevel(id)

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<MessageNotificationLevel> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.MessageNotificationLevel", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: MessageNotificationLevel) = encoder.encodeInt(value.id)

        override fun deserialize(decoder: Decoder): MessageNotificationLevel = when(val id = decoder.decodeInt()) {
            0 -> AllMessages
            1 -> OnlyMentions
            else -> Unknown(id)
        }
    }
}

@Serializable(with = ExplicitContentFilterLevel.Serializer::class)
sealed class ExplicitContentFilterLevel(val id: Int) {

    object Disabled : ExplicitContentFilterLevel(0)
    object MembersWithoutRoles : ExplicitContentFilterLevel(1)
    object AllMembers : ExplicitContentFilterLevel(2)

    class Unknown(id: Int) : ExplicitContentFilterLevel(id)

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<ExplicitContentFilterLevel> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.ExplicitContentFilterLevel", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: ExplicitContentFilterLevel) = encoder.encodeInt(value.id)

        override fun deserialize(decoder: Decoder): ExplicitContentFilterLevel = when(val id = decoder.decodeInt()) {
            0 -> Disabled
            1 -> MembersWithoutRoles
            2 -> AllMembers
            else -> Unknown(id)
        }
    }
}

@Serializable(with = GuildFeature.Serializer::class)
sealed class GuildFeature(val name: String) {

    object InviteSplash : GuildFeature("INVITE_SPLASH")
    object VipRegions : GuildFeature("VIP_REGIONS")
    object VanityUrl : GuildFeature("VANITY_URL")
    object Verified : GuildFeature("VERIFIED")
    object Partnered : GuildFeature("PARTNERED")
    object Community : GuildFeature("COMMUNITY")
    object Commerce : GuildFeature("COMMERCE")
    object News : GuildFeature("NEWS")
    object Discoverable : GuildFeature("DISCOVERABLE")
    object Featurable : GuildFeature("FEATURABLE")
    object AnimatedIcon : GuildFeature("ANIMATED_ICON")
    object Banner : GuildFeature("BANNER")
    object WelcomeScreenEnabled : GuildFeature("WELCOME_SCREEN_ENABLED")
    object Lurkable : GuildFeature("LURKABLE")
    object MoreEmoji : GuildFeature("MORE_EMOJI")
    object MembershipGatingEnabled : GuildFeature("MEMBER_VERIFICATION_GATE_ENABLED")
    object PreviewEnabled : GuildFeature("PREVIEW_ENABLED")

    class Unknown(name: String) : GuildFeature(name) {

        override fun toString(): String {
            return "Unknown ($name)"
        }
    }

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<GuildFeature> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.GuildFeature", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: GuildFeature) = encoder.encodeString(value.name)

        override fun deserialize(decoder: Decoder): GuildFeature = when(val name = decoder.decodeString()) {
            "INVITE_SPLASH" -> InviteSplash
            "VIP_REGIONS" -> VipRegions
            "VANITY_URL" -> VanityUrl
            "VERIFIED" -> Verified
            "PARTNERED" -> Partnered
            "COMMUNITY" -> Community
            "COMMERCE" -> Commerce
            "NEWS" -> News
            "DISCOVERABLE" -> Discoverable
            "FEATURABLE" -> Featurable
            "ANIMATED_ICON" -> AnimatedIcon
            "BANNER" -> Banner
            "WELCOME_SCREEN_ENABLED" -> WelcomeScreenEnabled
            "LURKABLE" -> Lurkable
            "MORE_EMOJI" -> MoreEmoji
            "PREVIEW_ENABLED" -> PreviewEnabled
            "MEMBER_VERIFICATION_GATE_ENABLED" -> MembershipGatingEnabled

            else -> Unknown(name)
        }
    }
}

@Serializable(with = MfaLevel.Serializer::class)
sealed class MfaLevel(val id: Int) {

    object None : MfaLevel(0)
    object Elevated : MfaLevel(1)

    class Unknown(id: Int) : MfaLevel(id)

    override fun toString(): String {
        return this::class.simpleName!!
    }

    object Serializer : KSerializer<MfaLevel> {

        override val descriptor = PrimitiveSerialDescriptor("Delta.MfaLevel", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, value: MfaLevel) = encoder.encodeInt(value.id)

        override fun deserialize(decoder: Decoder): MfaLevel = when(val id = decoder.decodeInt()) {
            0 -> None
            1 -> Elevated

            else -> Unknown(id)
        }
    }


}
