import requests

r = requests.post('http://127.0.0.1:8080/login', json={"login": "admin", "password": "goodpassword"})
#r = requests.post('http://95.26.136.23:80/login', json={"username": "mihail", "email": "mihail@mail.ru", "password": "12345678"})
#r = requests.post('http://127.0.0.1:8000/auth', json={"token": "4cd549907d6a41fc98a9e1b67ad3d61a"})

print(r.status_code)
print(r.json())
