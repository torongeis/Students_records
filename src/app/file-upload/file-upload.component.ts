import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-file-upload',
  standalone: true,
  imports: [CommonModule], // No need for HttpClientModule
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent {
  selectedFile!: File | null;
  isUploading: boolean = false;
  uploadSuccess: boolean = false;
  uploadError: string = '';

  constructor(private http: HttpClient) {}
  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.uploadSuccess = false;
    this.uploadError = '';
  }
  onDragOver(event: Event) {
    event.preventDefault();
  }
  onDrop(event: any) {
    event.preventDefault();
    const file = event.dataTransfer.files[0];
    if (file) {
      this.selectedFile = file;
    }
  }
  uploadFile() {
    if (!this.selectedFile) return;

    this.isUploading = true;
    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.http.post('http://localhost:8084/students/upload', formData).subscribe({
      next: () => {
        this.uploadSuccess = true;
        this.uploadError = '';
        this.isUploading = false;
        console.log('File successfully sent to the queue.');
      },
      error: (error) => {
        this.uploadError = 'File upload failed. It may not have been queued!';
        this.uploadSuccess = false;
        this.isUploading = false;
        console.error('File upload failed:', error);
      }
    });
  }
}
