import { Injectable } from '@angular/core';
import { ParallelHasher } from 'ts-md5/dist/parallel_hasher';


@Injectable()
export class Md5hashService {

  constructor() { }

  hashFile(file: File) {
    let hasher = new ParallelHasher('../assets/js/md5_worker.js');
    return hasher.hash(file);
  }

}
