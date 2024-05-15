import requests

r = requests.post('http://127.0.0.1:8080/testUser/register', json={"login": "qwerty1", "email": "qwerty1@mail.ru", "password": "goodpassword1"})

print(r.status_code)
print(r.json())

#r2 = requests.post('http://127.0.0.1:8080/testUser/test', json={"login": "qwerty6", "password": "goodpassword6"})

#print(r2.status_code)
#print(r2.json())



# надо добавить header: "Authorization: Bearer "
