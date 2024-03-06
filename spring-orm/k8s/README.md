# Kubernetes для разработчиков: Видеокурс

есть мастер нода - голова

есть ноды - аля физ машины

в поде всегда есть доп контейнер POD, несет в себе network namespace линуксового ядра

под минимальная абстракция

под всегда внутри одной ноды

docker-compose умеет оркестрирова только на одной тачке, k8s на нескольких за счет подключения к нескольким виртуалкам

под может быть одновременно  в нескольких версиях апи кубера

порт в ямле просто документирование

kubectl create -f pod.yml

переименовать имя и можно снова create делать - поднимется второй под

kubectl get pod

creating -> run

C:\Program Files\Docker\Docker\resources

config use-context docker-desktop

~/.kube - конфиг файл кубера

kubectl delete pod --all - удалить поды в неймспейсе

kubectl logs hello - вывести логи пода



## ReplicaSet

labels в любом конфиге для группировки объектов

template.lables - значит во всех подах созданных будут эти лейблы

spec.replicas - колво реплик

selector.matchLabels.label - будет считать своими подами все, которые имеют заданный лейбл

внутри реплика сет описываем темплейт нашего пода

kubectl create -f .\replicaset.yaml

kubectl get rs

my-replicaset(имя кластера)   2 (ожидали)        2(получилось)         2(готово)       14s

kubectl apply - может выполнять функцию create но является идемпотентной, если объект уже есть, ничего делать не будет, если нет, то создаст объект

Warning: resource replicasets/my-replicaset is missing the kubectl.kubernetes.io/last-applied-configuration annotation which is required by kubectl apply. kubectl apply should only be  used on resources created declaratively by either kubectl create --save-config or kubectl apply. The missing annotation will be patched automatically.

replicaset.apps/my-replicaset configured

kubectl scale replicaset my-replicaset --replicas 3

self-heeling - самоисцеляться

replicaset поддерживает кол-во реплик

попробуем запустить доп под указав labels.app: my-app

kubectl apply -f pod.yaml - состояние terminating уже лишний

kubectl edit replicaset my-replicaset - отредактировать объект nginx->1.13

kubectl describe [имя объекта под/репилкасет] - описание пода, самое важное - раздел events

видим в описании пода не поменялось

делаем delete pod, репликасет снова его поднимает и в нем уже 1.13

для того чтобы работать с обновлениами абстракция deployment

kubectl set image replicaset my-replicaset nginx=nginx:1.21 установить новую версию имеджа в репликасет


https://kubernetes.io/docs/reference/kubectl/jsonpath/

kubectl cheetsheet https://kubernetes.io/docs/reference/kubectl/cheatsheet/#viewing-finding-resources


sbox.slurm.io

подключиться по ssh с логином/паролем из памятки



## Deployment

kind: Deployment

Описание в простом случае совпадает с репликасет

kubectl apply -f deployment.yaml

kubectl get deploy

kubectl get rs

kubectl get po - в имени пода появлось имя деплоймента

Правило, работать с объектами, которые создали, т.е. не работать с rs, pod напрямую

kubectl set image deployment my-deployment '*=nginx:1.13' обновить образ в контейнере

можно еще с помощью команды kubedtl edit

kubectl explain deployment.spec.strategy - посмотреть инфо

на самом деле происходит ролинг апдейт, т.е. scale rs-1 1 один под прибивается, scale rs-2 1 поднимается, scale rs-1 0, scale rs-2 2

параметры сколько реплик можно погасить, сколько доп поднять, можно в процентах maxSurge и maxUnavailable

В основном эта абстракция используется на практике

Зад 1 kubectl get deployment my-deployment -o=jsonpath='{.status.conditions[1].message}{"\n"}'

Помогло ковычки поменять

Ответ ReplicaSet "my-deployment-58c4f5f5b" has successfully progressed.

kubectl delete deploy my-deployment

Зад 2 kubectl get deployment my-deployment -o custom-columns='NAME:.metadata.name,MAXSURGE:.spec.strategy.rollingUpdate.maxSurge,MAXUNAVAILABLE:.spec.strategy.rollingUpdate.maxUnavailable'

Это еще одна возможность ключа -o. Она позволяет вывести описание объекта с пользовательским набором полей.

my-deployment   <none>   <none>


## namespace

для создания объектов  с одним именем и типом

чтобы указать kubectl -n

чтобы применилось ко всем ключик -A


## resources

limit

память превышение прибивание oom-киллер, перезапуск пода

цпу превышение вызывает тротлинг - самост посмотр

requests

резервиреутся под под (не делится), вычитается из ресурсов ноды

например нода 2цпу, 4гб , запускаем 2 пода по 1цпу, 1гб все больше запустить не сможем

kubectl apply -f deployment.yaml

kubectl get pod

экспиремень request 10 cpu,  pod  describe  в евентах видим что не может подняться

закончилось capacity

kubectl patch deployment my-deployment --patch '{"spec":{"template":{"spec":{"containers":[{"name":"nginx","resources":{"requests":{"cpu":"10"},"limits":{"cpu":"10"}}}]}}}}'

kubectl describe po ..

Warning  FailedScheduling  2m21s  default-scheduler  0/1 nodes are available: 1 Insufficient cpu. preemption: 0/1 nodes are available: 1 No preemption victims found for incoming pod.



## комманды

kubectl create, apply, run --image, exec -t -i pod_name command, logs , get -o -n, describe, edit, set image, cp (copy file), [commad] --help, explain



## Environment

в деполймент

env:

- name: TEST

  value: foo

kubectl exec -it [pod] -- env - вывести енвайрмент переменные для пода

kubectl describe pod - там раздел Environment



## ConfigMap

kubectl describe cm [configmap]

               get cm -o yaml - вывод в ямл формате

kbl get deployments.apps

kbl create -f  configmap.yaml

kbl apply -f - переконфигурировать по идее к поду сразу не применяется, нужно перезапустить

посмотреть переменные exec -- env

## Secret  
kubectl create secret generic test --from-literal=test1=asdf --from-literal=dbpassword=rftgyh
  secret/test created
kubectl get secrets
kubectl describe secrets test
  есть настройки, но нет значений
kubectl get secrets test -o yaml
  данные в бейз 64
echo cmZ0Z3lo | base64 -d
  получаю пароль
secret не шифрует, а кодирует информацию
все равно есть смысл, т.к. к этой абстракции можно запретить доступ
с помощь kbctl decribe pod не увидим, но увидим ссылку на секрет
для корпораций лучше хранить в vault

можно сделать манифест secret.yaml
если делать apply(произойдет меш - слияние) сначала в манифесте добавить переменную test(в общем сектрете тоже добав),  
затем второй apply изменить имя на test1 будет сайд эффект:  
test примет пустое значение (не получилось - корректно отработало)

## ConfigMap-2
в yaml | значит многострочное значеие
секция volumes их может быть несколько и разных типов и подключаться к разным контейнерам внутри деплоймента
slurm-master-k8sdev
kubectl create -f создаем конфигмап с содержимым файла
kubectl describe cm [name]
kebectl apply -f к деплоймент, в volumes ссылаемся на конфигмап с содержимым файла.
kubectl get pod посмотреть mounts, volumes
kubectl exec -it [pod] -- bash
проверить /etc/nginx/conf.d там default.conf
ls -l ссылается
ls -lsa цепочка ссылок, симлинки приводят в каталог с таймстемпом, в этом каталоге уже файл.
в отличие от env файл внутри контейнера обновить можно, если в конфигмапе изменим, то спустя время измениться и файл,
как раз симлинки позволяют это делать, наверное файл кладется в новую папку с таймстемпом затем
Проверяем что nginx подхвотил файл и выдаст имя пода на http://[ip] внутри кластера
Если кластер где-то, с локального так не получится, чтобы это обойти делаем kubectl port-forward
kubectl port-forward my-deployment-5645d9c4f6-rrft2 8081:80 &
Как подложить в под файл, содержимое которого храниться в конфигмапе ?
Зачем файл конфигмапы монтируется через симлинки ?
Что нужно сделать чтобы curl обратиться к поду на удаленном кластере ?

Проверяем что при обновлении конфигмапа с содержанием файла, файл в поде тоже обновиться
изменяем my-configmap
файл обновился, но nginx его не перечитывает, т.к. ему надо дать команду reload
поэтому надо использовать cloud-native приложения, которые динамически перечитывают файл или env
kubectl edit cm my-configmap
volumeMounts:
- name: config
  mountPath: /etc/nginx/conf.d/
- name: php
  mountPath: /etc/php.ini
  subPath: php.ini
Во втором случае смонтировался без симлинка исправляем как в первом, вообще не стартует
  Warning  Failed     2s                 kubelet            Error: failed to start container "nginx": Error response from daemon: failed to create shim task: OCI runtime create failed: runc create failed: unable to start container process: error during container init: error mounting "/var/lib/docker/containers/f01f44606dd25cdc8ec4aec614dd492f5b454afb5a0242cc91308b3f5a3a8b31/resolv.conf" to rootfs at "/etc/resolv.conf": open /var/lib/docker/overlay2/cb64e115d93f4615b299d6f69259e1f1e1d6c1e3dd2f5f811b89e7c15b4c9574/merged/etc/resolv.conf: read-only file system: unknown
Получается subPath нужен при монтировании в существующий каталог?

## Downward API
Передать манифест окружения приложению в виде файла или env
Например namespace, ip ноды
kubect get po [name] -o yaml - там есть секция spec, .. - заполняется автоматически
```yaml
        - name: __NODE_IP
          valueFrom:
            fieldRef:
              fieldPath: status.hostIP
```
в deployment можем ссылаться на эту секцию особым образом
```yaml
      volumes
      - name: podinfo
        downwardAPI:
          items:
            - path: "labels" -- файлик с лейблами
              fieldRef:
                fieldPath: metadata.labels
            - path: "annotations" -- файлик с аннотациями
              fieldRef:
                fieldPath: metadata.annotations
```
