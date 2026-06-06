
Add-Type -AssemblyName System.IO.Compression.FileSystem
$docxPath = "E:\pharmacy\Pharmacy_Inventory_BA_Requirements (1).docx"
$zip = [System.IO.Compression.ZipFile]::OpenRead($docxPath)
$entry = $zip.Entries | Where-Object { $_.FullName -eq "word/document.xml" }
$stream = $entry.Open()
$reader = New-Object System.IO.StreamReader($stream)
$xmlContent = $reader.ReadToEnd()
$reader.Close()
$stream.Close()
$zip.Dispose()
$textNodes = [regex]::Matches($xmlContent, '<w:t.*?>(.*?)</w:t>')
foreach ($node in $textNodes) {
    Write-Host $node.Groups[1].Value -NoNewline
    Write-Host " " -NoNewline
}
