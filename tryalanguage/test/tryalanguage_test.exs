<query target="Package" fetchLimit="25" distinct="true">
	<select property="contentPackageAcceptTemplate.packageAcceptTemplate.mediaAcceptProfiles.id"/>
  <select property="id" label="id"/>
  <select property="name" label="nickname"/>
  <select property="=parentPackage != null" label="isClip"/>
  <select property="=childPackages?.size()" label="clipCount"/>
  <select property="content.id" label="contentId"/>
  <select property="=contents.size()" label="contentCount"/>
  <select property="category.id" label="categoryId"/>
  <select property="originalReleaseDateCombined" label="originalReleaseDateCombined"/>
  <select property="=#hasPermissionException(id, 'PACKAGE_VIEW_DOWNLOAD_PROXY_VIEW_STATE')" label="hasPermissionException"/>
  <select property="=#hasPermissionException(id, 'PACKAGE_RESTORE_MASTER_MEDIA_VIEW_STATE')" label="hasMasterMediaPermissionException"/>
  <select property="status" label="status"/>
  <select property="iCFStatus" label="iCFStatus"/>
  <select property="lockedFlag" label="lockedFlag"/>
  <select property="archivedFlag" label="archivedFlag"/>
  <select property="validForDelivery" label="validForDelivery"/>
  <select property="=primaryMediaPackageContent?.proxyContent?.contentUuid" label="proxyContentUuid"/>
  <select property="=primaryMediaPackageContent?.proxyContent?.id" label="proxyContentId"/>
  <select property="=primaryMediaPackageContent?.durationTimecode" label="duration"/>
  <select property="dateCreated" label="dateCreated"/>
  <select property="dateUpdated" label="dateUpdated"/>
  <select property="originalReleaseDate" label="originalReleaseDate"/>
  <select property="acceptStatus" label="acceptStatus"/>
  <select property="mediaValidationErrorCount" label="mediaValidationErrorCount"/>
  <select property="metadataValidationErrorCount" label="metadataValidationErrorCount"/>
  <select property="episodeTitle" label="episodeTitle"/>
  <select property="productionNumber" label="productionNumber"/>
  <select property="categoryType.name" label="category"/>
  <select property="categoryType" label="categoryType"/>
  <select property="=content?.name" label="seriesOrMovieName"/>
  <select property="=content?.type == 'Series' ? content.airTime : null" label="airTime"/>
  <select property="=content?.network?.name" label="networkName"/>
  <select property="=season?.name" label="seasonName"/>
  <select property="=volume?.name" label="volumeName"/>
  <select property="primaryAcceptTemplateMember.mediaAcceptProfile.name" label="videoMap"/>
  <select property="=partnerPackages.![id]" label="partnerPackageIds"/>
  <select property="=partnerPackages.?[enabledFlag == true].![id]" label="enabledPartnerPackageIds"/>
  <select property="=partnerPackages.?[enabledFlag == true and partner != null].![partner.id]" label="partners"/>
  <criteria>
    <and>
      <condition property="parentPackage" op="isnull" nullTargets="true"/>
      <condition property="content.type" op="eq" nullTargets="false">
        <test value="Series"/>
      </condition>
      <condition property="categoryType.name" op="eq" nullTargets="false">
        <test value="Short Form"/>
      </condition>
      <or>
        <condition property="content.name" op="eq" nullTargets="true">
          <test value="Extreme Makeover Weight Loss Edition"/>
        </condition>
        <condition property="content.name" op="eq" nullTargets="true">
          <test value="Extreme Weight Loss"/>
        </condition>
      </or>
    </and>
  </criteria>
  <sort-order property="dateCreated" direction="DESC"/>
</query>