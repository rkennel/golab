package me.ryan.kennel.golab.webexteams

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import org.junit.Before


/**
 * Created by rkennel on 2/10/19.
 */
internal class WebexTeamsPropertiesServiceTest {

    val webexTeamsPropertiesService = WebexTeamsPropertiesService()

    @Before
    fun setup(){
        System.setProperty(com.google.datastore.v1.client.DatastoreHelper.LOCAL_HOST_ENV_VAR,"http://localhost:8081")
        System.setProperty(com.google.datastore.v1.client.DatastoreHelper.PROJECT_ID_ENV_VAR,"golab-231317")
    }

    @After
    fun cleanup(){
        val resultList = webexTeamsPropertiesService.retrieveAllEntities()

        resultList?.forEach { entity -> webexTeamsPropertiesService.deleteEntity(entity) }
    }



    @Test
    fun ifNoPropertyExistsSaveProperty(){

        val propertiesToSave = WebexTeamsProperties("token")

        val key = webexTeamsPropertiesService.save(propertiesToSave)

        assertThat(key).isGreaterThan(-1L)
    }

    @Test
    fun ifPropertyExistsThenDeleteAndSaveNewProperty(){
        val propertiesToSave = WebexTeamsProperties("token")
        val key = webexTeamsPropertiesService.save(propertiesToSave)

        val propertiesToSave2 = WebexTeamsProperties("token2")
        val key2 = webexTeamsPropertiesService.save(propertiesToSave2)

        assertThat(key2).isNotEqualTo(key)
        assertThat(sizeOfDatastore()).isEqualTo(1)
    }

    private fun sizeOfDatastore(): Int {
        var counter=0;
        webexTeamsPropertiesService.retrieveAllEntities()?.forEach {
            entity -> counter++
        }

        return counter
    }

    @Test
    fun iCanRetrieveSavedProperties(){

        val propertiesToSave = WebexTeamsProperties("token1")
        webexTeamsPropertiesService.save(propertiesToSave)

        val webexTeamsProperties = webexTeamsPropertiesService.retrieve()

        assertThat(webexTeamsProperties).isEqualToComparingFieldByField(propertiesToSave)
    }

    @Test
    fun ifNoSavedPropertiesThenRetrieveDefaultValue(){

        val webexTeamsProperties = webexTeamsPropertiesService.retrieve()

        assertThat(webexTeamsProperties).isEqualToComparingFieldByField(WebexTeamsProperties())
    }



}


