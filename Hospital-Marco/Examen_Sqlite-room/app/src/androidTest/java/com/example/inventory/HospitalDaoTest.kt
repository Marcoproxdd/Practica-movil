/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.inventory.data.InventoryDatabase
import com.example.inventory.data.Hospital
import com.example.inventory.data.HospitalDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class HospitalDaoTest {

    private lateinit var hospitalDao: HospitalDao
    private lateinit var inventoryDatabase: InventoryDatabase
    private val hospital1 = Hospital(1, "Guatemala", "Pimocha", "Premium", 500)
    private val hospital2 = Hospital(2, "Colombia", "Australia", "Economica", 250)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        inventoryDatabase = Room.inMemoryDatabaseBuilder(context, InventoryDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        hospitalDao = inventoryDatabase.itemDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        inventoryDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoInsert_insertsItemIntoDB() = runBlocking {
        addOneItemToDb()
        val allItems = hospitalDao.getAllItems().first()
        assertEquals(allItems[0], hospital1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllItems_returnsAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        val allItems = hospitalDao.getAllItems().first()
        assertEquals(allItems[0], hospital1)
        assertEquals(allItems[1], hospital2)
    }


    @Test
    @Throws(Exception::class)
    fun daoGetItem_returnsItemFromDB() = runBlocking {
        addOneItemToDb()
        val item = hospitalDao.getItem(1)
        assertEquals(item.first(), hospital1)
    }

    @Test
    @Throws(Exception::class)
    fun daoDeleteItems_deletesAllItemsFromDB() = runBlocking {
        addTwoItemsToDb()
        hospitalDao.delete(hospital1)
        hospitalDao.delete(hospital2)
        val allItems = hospitalDao.getAllItems().first()
        assertTrue(allItems.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun daoUpdateItems_updatesItemsInDB() = runBlocking {
        addTwoItemsToDb()
        hospitalDao.update(Hospital(1, "Guatemala", "Pimocha", "Premium", 500))
        hospitalDao.update(Hospital(2, "Colombia", "Australia", "Economica", 250))

        val allItems = hospitalDao.getAllItems().first()
        assertEquals(allItems[0], Hospital(1, "Guatemala", "Pimocha", "Premium", 500))
        assertEquals(allItems[1], Hospital(2, "Colombia", "Australia", "Economica", 250))
    }

    private suspend fun addOneItemToDb() {
        hospitalDao.insert(hospital1)
    }

    private suspend fun addTwoItemsToDb() {
        hospitalDao.insert(hospital1)
        hospitalDao.insert(hospital2)
    }
}
