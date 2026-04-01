{{- define "mp3-storage.labels" -}}
date: {{ now | date "02-01-2006" }}
version: {{ .Chart.Version }}
{{- end }}