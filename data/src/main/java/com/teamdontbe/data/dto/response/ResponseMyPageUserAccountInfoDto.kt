import com.teamdontbe.domain.entity.MyPageUserAccountInfoEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMyPageUserAccountInfoDto(
    @SerialName("joinDate") val joinDate: String,
    @SerialName("memberId") val memberId: Int,
    @SerialName("showMemberId") val showMemberId: String,
    @SerialName("socialPlatform") val socialPlatform: String,
    @SerialName("versionInformation") val versionInformation: String,
) {
    fun toMyPageUserAccountInfoEntity() = MyPageUserAccountInfoEntity(
        joinDate,
        memberId,
        showMemberId,
        socialPlatform,
        versionInformation,
    )
}
