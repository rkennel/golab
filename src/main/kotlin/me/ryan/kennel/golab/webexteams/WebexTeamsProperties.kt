package me.ryan.kennel.golab.webexteams

import com.google.cloud.datastore.DatastoreOptions
import com.google.cloud.datastore.Entity
import com.google.cloud.datastore.Query
import com.google.cloud.datastore.QueryResults
import java.io.Serializable

class WebexTeamsProperties(val token: String="") : Serializable {

    companion object {
        val TOKEN = "token"
    }


}

class WebexTeamsPropertiesService {

    val datastore = DatastoreOptions.getDefaultInstance().getService()
    val keyFactory = datastore.newKeyFactory().setKind(WebexTeamsProperties::class.qualifiedName)

    fun save(webexTeamProperties: WebexTeamsProperties): Long {

        retrieveAllEntities()?.forEach { entity -> deleteEntity(entity) }

        return saveNewEntity(webexTeamProperties)
    }

    fun deleteEntity(entity: Entity) {
        datastore.delete(entity.key)
    }

    private fun saveNewEntity(webexTeamProperties: WebexTeamsProperties): Long {
        val key = keyFactory.newKey()

        val entityToSave = Entity.newBuilder(key)
                .set(WebexTeamsProperties.TOKEN, webexTeamProperties.token)
                .build()

        return datastore.add(entityToSave).key.id
    }

    fun retrieve(): WebexTeamsProperties {
        val resultList = retrieveAllEntities()

        if(resultList.hasNext()){
            return convertEntityToProperties(resultList.next())
        }
        else{
            return WebexTeamsProperties();
        }
    }

    internal fun retrieveAllEntities(): QueryResults<Entity> {
        val query = Query.newEntityQueryBuilder()       // Build the Query
                .setKind(WebexTeamsProperties::class.qualifiedName)                                     // We only care about Books
                .setLimit(1)
                .setStartCursor(null)
                .build()

        return datastore.run(query)

    }

    private fun convertEntityToProperties(entity: Entity): WebexTeamsProperties {
        return WebexTeamsProperties(entity?.getString(WebexTeamsProperties.TOKEN))
    }

}