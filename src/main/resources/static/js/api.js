const BASE_URL = "http://localhost:8080";

async function fetchDailyRecord(userId, date) {
  const url = date
    ? `${BASE_URL}/api/daily-records/${userId}?date=${date}`
    : `${BASE_URL}/api/daily-records/${userId}`;

  const res = await fetch(url);
  const json = await res.json();
  return json.data;
}

async function fetchMonthlyRecords(userId, year, month) {
  const res = await fetch(
    `${BASE_URL}/api/daily-records/${userId}/monthly?year=${year}&month=${month}`
  );
  const json = await res.json();
  return json.data; // date 배열
}

async function updateFoodConsumed(foodId) {
  const res = await fetch(`${BASE_URL}/api/foods/${foodId}`, {
    method: "PUT",
  });

  const json = await res.json();
  return json.data; // 수정된 Food
}

async function updateActivityCompleted(activityId) {
  const res = await fetch(`${BASE_URL}/api/activities/${activityId}`, {
    method: "PUT",
  });

  const json = await res.json();
  return json.data; // 수정된 Activity
}

async function saveWalkDetail(activityId, detail) {
  await fetch(`${BASE_URL}/api/activities/${activityId}/walk-detail`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(detail),
  });
}

async function createFood(payload) {
  const res = await fetch(`${BASE_URL}/api/foods`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!res.ok) throw new Error("Food 생성 실패");

  const json = await res.json();
  return json.data;
}

async function createActivity(payload) {
  const res = await fetch(`${BASE_URL}/api/activities`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(payload),
  });

  if (!res.ok) throw new Error("Activity 생성 실패");

  const json = await res.json();
  return json.data;
}

async function updateMemo(dailyRecordId, memo) {
  const res = await fetch(
    `${BASE_URL}/api/daily-records/${dailyRecordId}/memo`,
    {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ memo }),
    }
  );

  if (!res.ok) {
    throw new Error("Memo 저장 실패");
  }

  const json = await res.json();
  return json.data;
}
